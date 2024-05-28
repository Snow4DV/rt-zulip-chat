package ru.snowadv.chat_presentation.chat.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.ChangeMessageReadStateUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toElmEvent
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.presentation.elm.compat.SwitchingActor
import javax.inject.Inject

@Reusable
class ChatActorElm @Inject constructor(
    private val router: ChatRouter,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetCurrentMessagesUseCase,
    private val listenToChatEventsUseCase: ListenToChatEventsUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
    private val sendFileUseCase: SendFileUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val loadMessageUseCase: LoadMessageUseCase,
    private val markMessagesAsReadUseCase: ChangeMessageReadStateUseCase,
) : SwitchingActor<ChatCommandElm, ChatEventElm>() {
    override fun execute(command: ChatCommandElm): Flow<ChatEventElm> = when (command) {
        ChatCommandElm.GoBack -> flow { router.goBack() }
        is ChatCommandElm.LoadInitialMessages -> getMessagesUseCase(
            command.streamName,
            command.topicName
        ).map { res ->
            when (res) {
                is Resource.Error -> ChatEventElm.Internal.Error(res.throwable, res.data)
                is Resource.Loading -> res.getDataOrNull()
                    ?.let { ChatEventElm.Internal.InitialChatLoadedFromCache(it) }
                    ?: ChatEventElm.Internal.Loading

                is Resource.Success -> ChatEventElm.Internal.InitialChatLoaded(res.data)
            }
        }

        is ChatCommandElm.LoadMoreMessages -> {
            loadMoreMessagesUseCase(
                streamName = command.streamName,
                topicName = command.topicName,
                firstLoadedMessageId = command.firstLoadedMessageId,
                includeAnchor = command.includeAnchor,
            ).map { res ->
                when (res) {
                    is Resource.Error -> ChatEventElm.Internal.PaginationError
                    is Resource.Loading -> ChatEventElm.Internal.PaginationLoading
                    is Resource.Success -> ChatEventElm.Internal.MoreMessagesLoaded(res.data)
                }
            }
        }

        is ChatCommandElm.ObserveEvents -> {
            listenToChatEventsUseCase(
                isRestart = command.isRestart,
                eventQueueProps = command.queueProps,
                streamName = command.streamName,
                topicName = command.topicName,
            ).map { it.toElmEvent() }.asSwitchFlow(command)
        }

        is ChatCommandElm.CancelObservation -> {
            cancelSwitchFlow(ChatCommandElm.ObserveEvents::class)
        }

        is ChatCommandElm.AddChosenReaction -> {
            addReactionUseCase(command.messageId, command.reactionName).map { res ->
                when (res) {
                    is Resource.Error -> ChatEventElm.Internal.ChangingReactionError(
                        ChatEventElm.Ui.AddChosenReaction(
                            command.messageId,
                            command.reactionName
                        )
                    )

                    is Resource.Loading -> ChatEventElm.Internal.ChangingReaction
                    is Resource.Success -> ChatEventElm.Internal.ReactionChanged
                }
            }
        }

        is ChatCommandElm.RemoveReaction -> {
            removeReactionUseCase(command.messageId, command.reactionName).map { res ->
                when (res) {
                    is Resource.Error -> ChatEventElm.Internal.ChangingReactionError(
                        ChatEventElm.Ui.RemoveReaction(
                            command.messageId,
                            command.reactionName
                        )
                    )

                    is Resource.Loading -> ChatEventElm.Internal.ChangingReaction
                    is Resource.Success -> ChatEventElm.Internal.ReactionChanged
                }
            }
        }

        is ChatCommandElm.SendMessage -> {
            sendMessageUseCase(command.streamName, command.topicName, command.text).map { res ->
                when (res) {
                    is Resource.Error -> ChatEventElm.Internal.SendingMessageError
                    is Resource.Loading -> ChatEventElm.Internal.SendingMessage
                    is Resource.Success -> ChatEventElm.Internal.MessageSent(command.topicName)
                }
            }
        }

        is ChatCommandElm.AddAttachment -> {
            sendFileUseCase(
                command.streamName,
                command.topicName,
                command.inputStreamOpener,
                command.mimeType,
                command.extension
            ).map { res ->
                when (res) {
                    is Resource.Error -> ChatEventElm.Internal.UploadingFileError(
                        ChatEventElm.Ui.FileWasChosen(
                            command.mimeType,
                            command.inputStreamOpener,
                            command.extension,
                        )
                    )

                    is Resource.Loading -> ChatEventElm.Internal.UploadingFile
                    is Resource.Success -> ChatEventElm.Internal.FileUploaded
                }
            }
        }

        is ChatCommandElm.LoadTopicsFromCurrentStream -> {
            getTopicsUseCase(command.streamId).map{ res ->
                ChatEventElm.Internal.TopicsResourceChanged(res.map { topics -> topics.map { it.name } })
            }
        }

        is ChatCommandElm.LoadMovedMessage -> {
            loadMessageUseCase(
                messageId = command.messageId,
                streamName = command.streamName,
                applyMarkdown = true,
            ).mapEvents(
                eventMapper = { res ->
                    when(res) {
                        is Resource.Error -> ChatEventElm.Internal.ErrorFetchingMovedMessage(res.throwable, command.requestQueueId, command.requestEventId)
                        is Resource.Loading -> null
                        is Resource.Success -> ChatEventElm.Internal.LoadedMovedMessage(res.data, command.requestQueueId, command.requestEventId)
                    }
                },
                errorMapper = { error ->
                    ChatEventElm.Internal.ErrorFetchingMovedMessage(error, command.requestQueueId, command.requestEventId)
                },
            )
        }

        is ChatCommandElm.MarkMessagesAsRead -> {
            // Marking messages as read happens in background and will retry on next data fetch
            // if internet is not present at the moment / other error has occurred
            markMessagesAsReadUseCase(command.messagesIds, true).mapEvents { null }
        }
    }

}