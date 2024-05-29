package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserCommandElm

sealed interface MessageEditorCommandElm {
    data class LoadMessage(val messageId: Long, val streamName: String) : MessageEditorCommandElm
    data class EditMessage(val messageId: Long, val newContent: String) : MessageEditorCommandElm
}