package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

internal sealed interface MessageEditorEventElm {
    sealed interface Ui : MessageEditorEventElm {
        data object Init : Ui
        data class ChangedContentText(val newContent: String) : Ui
        data object OnApplyClicked : Ui
        data object OnRetryClicked : Ui
    }

    sealed interface Internal : MessageEditorEventElm {
        data object LoadingMessage : Internal
        data class MessageLoaded(val content: String) : Internal
        data class MessageLoadingError(val error: Throwable, val errorMessage: String?) : Internal

        data object MessageEditing : Internal
        data object MessageEdited : Internal
        data class MessageEditingError(val error: Throwable, val errorMessage: String?) : Internal
    }
}