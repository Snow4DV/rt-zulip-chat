package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class MessageEditorReducerElm @Inject constructor() :
    ScreenDslReducer<MessageEditorEventElm, MessageEditorEventElm.Ui, MessageEditorEventElm.Internal, MessageEditorStateElm, MessageEditorEffectElm, MessageEditorCommandElm>(
        uiEventClass = MessageEditorEventElm.Ui::class,
        internalEventClass = MessageEditorEventElm.Internal::class,
    ) {
    override fun Result.internal(event: MessageEditorEventElm.Internal) {
        when(event) {
            MessageEditorEventElm.Internal.LoadingMessage -> state {
                copy(editorState = ScreenState.Loading())
            }
            MessageEditorEventElm.Internal.MessageEdited -> {
                state {
                    copy(editingMessage = false)
                }
                effects {
                    +MessageEditorEffectElm.CloseWithResult(MessageEditorResult.EditedMessage(state.messageId))
                }
            }
            MessageEditorEventElm.Internal.MessageEditing -> state {
                copy(editingMessage = true)
            }
            is MessageEditorEventElm.Internal.MessageEditingError -> {
                state {
                    copy(editingMessage = false)
                }
                effects {
                    +MessageEditorEffectElm.FinishWithError(event.error, event.errorMessage)
                }
            }
            is MessageEditorEventElm.Internal.MessageLoaded -> state {
                copy(editorState = ScreenState.Success(event.content))
            }
            is MessageEditorEventElm.Internal.MessageLoadingError -> state {
                copy(editorState = ScreenState.Error(event.error))
            }
        }
    }

    override fun Result.ui(event: MessageEditorEventElm.Ui) {
        when(event) {
            is MessageEditorEventElm.Ui.ChangedContentText -> state {
                copy(editorState = editorState.map { event.newContent })
            }
            MessageEditorEventElm.Ui.OnRetryClicked, MessageEditorEventElm.Ui.Init -> commands {
                +MessageEditorCommandElm.LoadMessage(state.messageId, state.streamName)
            }
            MessageEditorEventElm.Ui.OnApplyClicked -> commands {
                state.editorState.data?.let { text ->
                    +MessageEditorCommandElm.EditMessage(state.messageId, text)
                }
            }
        }
    }

}