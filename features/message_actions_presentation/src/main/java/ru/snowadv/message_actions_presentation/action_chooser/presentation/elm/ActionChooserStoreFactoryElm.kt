package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class ActionChooserStoreFactoryElm @Inject constructor(
    private val actor: Actor<ActionChooserCommandElm, ActionChooserEventElm>,
    private val reducer: Provider<ScreenDslReducer<ActionChooserEventElm, ActionChooserEventElm.Ui, ActionChooserEventElm.Internal, ActionChooserStateElm, ActionChooserEffectElm, ActionChooserCommandElm>>,
) {

    companion object {
        private val initialOwnerActions by lazy {
            listOf(
                MessageAction.AddReaction(),
                MessageAction.RemoveMessage(),
                MessageAction.EditMessage(),
                MessageAction.MoveMessage(),
                MessageAction.CopyMessage(),
                MessageAction.OpenSenderProfile(),
                MessageAction.ReloadMessage(),
            )
        }
        private val initialNotOwnerActions by lazy {
            listOf(
                MessageAction.AddReaction(),
                MessageAction.CopyMessage(),
                MessageAction.OpenSenderProfile(),
                MessageAction.ReloadMessage(),
            )
        }
    }

    fun create(
        messageId: Long,
        senderId: Long,
        streamName: String,
        isOwner: Boolean
    ): ElmStore<ActionChooserEventElm, ActionChooserStateElm, ActionChooserEffectElm, ActionChooserCommandElm> {
        return ElmStore(
            initialState = ActionChooserStateElm(
                messageId = messageId,
                senderId = senderId,
                actions = if (isOwner) initialOwnerActions else initialNotOwnerActions,
                streamName = streamName,
                isOwner = isOwner,
            ),
            actor = actor,
            reducer = reducer.get(),
        )
    }
}