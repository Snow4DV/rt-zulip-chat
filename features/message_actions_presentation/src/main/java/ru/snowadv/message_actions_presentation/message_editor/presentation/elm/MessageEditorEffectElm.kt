package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult

internal sealed interface MessageEditorEffectElm {
    data class CloseWithResult(val result: MessageEditorResult): MessageEditorEffectElm
    data class FinishWithError(val throwable: Throwable, val errorMessage: String?): MessageEditorEffectElm
}