package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult
import ru.snowadv.message_actions_presentation.api.model.MessageMoveResult

internal sealed interface MessageTopicChangerEffectElm {
    data class CloseWithResult(val result: MessageMoveResult): MessageTopicChangerEffectElm
    data class FinishWithError(val throwable: Throwable, val errorMessage: String?):
        MessageTopicChangerEffectElm
}