package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

import ru.snowadv.message_actions_presentation.api.model.MessageMoveResult
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorCommandElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEffectElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEventElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorStateElm
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class MessageTopicChangerReducerElm @Inject constructor() :
    ScreenDslReducer<MessageTopicChangerEventElm, MessageTopicChangerEventElm.Ui, MessageTopicChangerEventElm.Internal, MessageTopicChangerStateElm, MessageTopicChangerEffectElm, MessageTopicChangerCommandElm>(
        uiEventClass = MessageTopicChangerEventElm.Ui::class,
        internalEventClass = MessageTopicChangerEventElm.Internal::class,
    ) {
    override fun Result.internal(event: MessageTopicChangerEventElm.Internal) {
        when(event) {
            is MessageTopicChangerEventElm.Internal.LoadedTopics -> state {
                copy(topics = ScreenState.Success(event.topics))
            }
            MessageTopicChangerEventElm.Internal.MessageMoved -> {
                state {
                    copy(movingMessage = false)
                }
                effects {
                    +MessageTopicChangerEffectElm.CloseWithResult(MessageMoveResult.MovedMessage(state.messageId))
                }
            }
            is MessageTopicChangerEventElm.Internal.MessageMovingError -> effects {
                +MessageTopicChangerEffectElm.FinishWithError(event.error, event.errorMessage)
            }
            MessageTopicChangerEventElm.Internal.MovingMessage -> state {
                copy(movingMessage = true)
            }
            MessageTopicChangerEventElm.Internal.LoadingTopics -> state {
                copy(topics = ScreenState.Loading())
            }
            is MessageTopicChangerEventElm.Internal.LoadingTopicsError -> state {
                copy(topics = ScreenState.Error(event.error))
            }
        }
    }

    override fun Result.ui(event: MessageTopicChangerEventElm.Ui) {
        when(event) {
            is MessageTopicChangerEventElm.Ui.ChangedNotifyNewTopic -> state {
                copy(notifyNewTopic = event.checked)
            }
            is MessageTopicChangerEventElm.Ui.ChangedNotifyOldTopic -> state {
                copy(notifyOldTopic = event.checked)
            }
            is MessageTopicChangerEventElm.Ui.ChangedTopic -> state {
                copy(currentTopicName = event.newTopic)
            }
            MessageTopicChangerEventElm.Ui.Init, MessageTopicChangerEventElm.Ui.OnRetryClicked -> commands {
                +MessageTopicChangerCommandElm.LoadTopics(state.streamId)
            }

            MessageTopicChangerEventElm.Ui.OnMoveClicked -> commands {
                +MessageTopicChangerCommandElm.MoveMessageToOtherTopic(
                    messageId = state.messageId,
                    newTopic = state.currentTopicName,
                    notifyOldThread = state.notifyOldTopic,
                    notifyNewThread = state.notifyNewTopic,
                )
            }
        }
    }

}