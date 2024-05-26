package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

import ru.snowadv.model.ScreenState

internal data class MessageEditorStateElm(
    val messageId: Long,
    val streamName: String,
    val editorState: ScreenState<String>,
    val editingMessage: Boolean,
)