package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class MessageEditorStoreFactoryElm @Inject constructor(
    private val actor: Actor<MessageEditorCommandElm, MessageEditorEventElm>,
    private val reducer: Provider<ScreenDslReducer<MessageEditorEventElm, MessageEditorEventElm.Ui, MessageEditorEventElm.Internal, MessageEditorStateElm, MessageEditorEffectElm, MessageEditorCommandElm>>,
) {

    companion object {
        private val initialOwnerActions = listOf(
            MessageAction.AddReaction(),
            MessageAction.RemoveMessage(),
            MessageAction.EditMessage(),
            MessageAction.MoveMessage(),
            MessageAction.CopyMessage(),
            MessageAction.OpenSenderProfile(),
        )
        private val initialNotOwnerActions = listOf(
            MessageAction.AddReaction(),
            MessageAction.CopyMessage(),
            MessageAction.OpenSenderProfile(),
        )
    }

    fun create(
        messageId: Long,
        streamName: String,
    ): ElmStore<MessageEditorEventElm, MessageEditorStateElm, MessageEditorEffectElm, MessageEditorCommandElm> {
        return ElmStore(
            initialState = MessageEditorStateElm(
                messageId = messageId,
                streamName = streamName,
                editorState = ScreenState.Loading(),
                editingMessage = false,
            ),
            actor = actor,
            reducer = reducer.get(),
            startEvent = MessageEditorEventElm.Ui.Init,
        )
    }
}