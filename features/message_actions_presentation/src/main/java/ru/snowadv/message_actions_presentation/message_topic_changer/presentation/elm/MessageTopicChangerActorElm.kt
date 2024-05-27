package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.MoveMessageToOtherTopicUseCase
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorCommandElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEventElm
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class MessageTopicChangerActorElm @Inject constructor(
    private val getTopicsUseCase: GetTopicsUseCase,
    private val moveMessageToOtherTopicUseCase: MoveMessageToOtherTopicUseCase,
) : Actor<MessageTopicChangerCommandElm, MessageTopicChangerEventElm>() {
    override fun execute(command: MessageTopicChangerCommandElm): Flow<MessageTopicChangerEventElm> {
        return when(command) {
            is MessageTopicChangerCommandElm.LoadTopics -> getTopicsUseCase(streamId = command.streamId)
                .mapEvents(
                    eventMapper = { res ->
                        when(res) {
                            is Resource.Error -> MessageTopicChangerEventElm.Internal.LoadingTopicsError(res.throwable, res.error)
                            is Resource.Loading -> res.data?.let { topics ->
                                MessageTopicChangerEventElm.Internal.LoadedTopics(topics.map { it.name })
                            } ?: MessageTopicChangerEventElm.Internal.LoadingTopics
                            is Resource.Success -> MessageTopicChangerEventElm.Internal.LoadedTopics(res.data.map { it.name })
                        }
                    },
                    errorMapper = { error ->
                        MessageTopicChangerEventElm.Internal.LoadingTopicsError(error, null)
                    }
                )
            is MessageTopicChangerCommandElm.MoveMessageToOtherTopic -> {
                moveMessageToOtherTopicUseCase(
                    messageId = command.messageId,
                    newTopic = command.newTopic,
                    notifyNewThread = command.notifyNewThread,
                    notifyOldThread = command.notifyOldThread,
                ).mapEvents(
                    eventMapper = { res ->
                        when(res) {
                            is Resource.Error -> MessageTopicChangerEventElm.Internal.MessageMovingError(res.throwable, res.error)
                            is Resource.Loading -> MessageTopicChangerEventElm.Internal.MovingMessage
                            is Resource.Success -> MessageTopicChangerEventElm.Internal.MessageMoved(command.newTopic)
                        }
                    },
                    errorMapper = { error ->
                        MessageTopicChangerEventElm.Internal.MessageMovingError(error, null)
                    },
                )
            }
        }
    }


}