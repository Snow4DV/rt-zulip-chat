package ru.snowadv.chat_impl.presentation.emoji_chooser.elm

import ru.snowadv.presentation.model.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class EmojiChooserStateFactoryElm(
    private val actor: EmojiChooserActorElm,
) {

    fun create(): Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm> {
        return ElmStore(
            initialState = EmojiChooserStateElm(screenState = ScreenState.Loading),
            actor = actor,
            reducer = EmojiChooserReducerElm(),
            startEvent = EmojiChooserEventElm.Ui.Init,
        )
    }
}