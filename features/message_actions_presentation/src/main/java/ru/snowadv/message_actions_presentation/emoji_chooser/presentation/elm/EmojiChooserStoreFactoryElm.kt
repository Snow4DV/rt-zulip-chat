package ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm

import dagger.Reusable
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class EmojiChooserStoreFactoryElm @Inject constructor(
    private val actor: Actor<EmojiChooserCommandElm, EmojiChooserEventElm>,
    private val reducer: Provider<ScreenDslReducer<EmojiChooserEventElm, EmojiChooserEventElm.Ui, EmojiChooserEventElm.Internal, EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserCommandElm>>,
) {

    fun create(excludeEmojisNames: Set<String>): Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm> {
        return ElmStore(
            initialState = EmojiChooserStateElm(
                screenState = ScreenState.Loading(),
                excludeEmojisNames = excludeEmojisNames,
            ),
            actor = actor,
            reducer = reducer.get(),
            startEvent = EmojiChooserEventElm.Ui.Init,
        )
    }
}