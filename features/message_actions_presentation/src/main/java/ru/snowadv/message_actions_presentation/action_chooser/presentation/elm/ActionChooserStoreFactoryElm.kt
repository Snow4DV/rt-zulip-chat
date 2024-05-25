package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserCommandElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class ActionChooserStoreFactoryElm @Inject constructor(
    private val actor: Actor<ActionChooserCommandElm, ActionChooserEventElm>,
    private val reducer: Provider<ScreenDslReducer<ActionChooserEventElm, ActionChooserEventElm.Ui, ActionChooserEventElm.Internal, ActionChooserStateElm, ActionChooserEffectElm, ActionChooserCommandElm>>,
) {

    companion object {
        private val initialActions = listOf(
            MessageAction.AddReaction(),
            MessageAction.RemoveMessage(),
            MessageAction.EditMessage(),
            MessageAction.MoveMessage(),
            MessageAction.CopyMessage(),
            MessageAction.OpenSenderProfile(),
        )
    }

    fun create(messageId: Long, senderId: Long): ElmStore<ActionChooserEventElm, ActionChooserStateElm, ActionChooserEffectElm, ActionChooserCommandElm> {
        return ElmStore(
            initialState = ActionChooserStateElm(messageId = messageId, senderId = senderId, actions = initialActions),
            actor = actor,
            reducer = reducer.get(),
        )
    }
}