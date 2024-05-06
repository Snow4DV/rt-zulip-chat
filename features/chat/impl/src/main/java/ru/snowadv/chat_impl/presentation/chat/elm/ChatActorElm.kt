package ru.snowadv.chat_impl.presentation.chat.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.chat_impl.domain.use_case.AddReactionUseCase
import ru.snowadv.chat_impl.domain.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_impl.domain.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_impl.domain.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_impl.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat_impl.domain.use_case.SendMessageUseCase
import ru.snowadv.chat_impl.presentation.util.ChatMappers.toElmEvent
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class ChatActorElm @Inject constructor(
    private val router: ChatRouter,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetCurrentMessagesUseCase,
    private val listenToChatEventsUseCase: ListenToChatEventsUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
) : Actor<ChatCommandElm, ChatEventElm>() {
    override fun execute(command: ChatCommandElm): Flow<ChatEventElm> = when (command) {
        ChatCommandElm.GoBack -> flow { router.goBack() }
        is ChatCommandElm.LoadInitialMessages -> getMessagesUseCase(
            command.streamName,
            command.topicName
        ).map { res ->
            when (res) {
                is Resource.Error -> ChatEventElm.Internal.Error(res.throwable, res.data)
                is Resource.Loading -> res.getDataOrNull()
                    ?.let { ChatEventElm.Internal.InitialChatLoaded(it, true) }
                    ?: ChatEventElm.Internal.Loading
                is Resource.Success -> ChatEventElm.Internal.InitialChatLoaded(res.data, false)
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
            ).map { it.toElmEvent() }
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

        is ChatCommandElm.GoToProfile -> flow { router.openProfile(command.profileId) }
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
                    is Resource.Success -> ChatEventElm.Internal.MessageSent
                }
            }
        }
    }

}