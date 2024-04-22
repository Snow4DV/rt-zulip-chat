package ru.snowadv.chat.presentation.emoji_chooser.elm

import ru.snowadv.chat.presentation.chat.elm.ChatCommandElm
import ru.snowadv.chat.presentation.chat.elm.ChatEffectElm
import ru.snowadv.chat.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat.presentation.chat.elm.ChatStateElm
import ru.snowadv.presentation.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

internal class EmojiChooserReducerElm :
    ScreenDslReducer<EmojiChooserEventElm, EmojiChooserEventElm.Ui, EmojiChooserEventElm.Internal, EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserCommandElm>(
        uiEventClass = EmojiChooserEventElm.Ui::class,
        internalEventClass = EmojiChooserEventElm.Internal::class,
    ) {
    override fun Result.internal(event: EmojiChooserEventElm.Internal) {
        when(event) {
            EmojiChooserEventElm.Internal.EmojiLoadError -> state {
                copy(screenState = ScreenState.Error())
            }
            EmojiChooserEventElm.Internal.EmojiLoading -> state {
                copy(screenState = ScreenState.Loading)
            }
            is EmojiChooserEventElm.Internal.LoadedEmojis -> state {
                copy(screenState = ScreenState.Success(event.emojis))
            }
        }
    }

    override fun Result.ui(event: EmojiChooserEventElm.Ui) {
        when(event) {
            is EmojiChooserEventElm.Ui.OnEmojiChosen -> effects {
                +EmojiChooserEffectElm.CloseWithChosenEmoji(event.emoji)
            }
            EmojiChooserEventElm.Ui.OnRetryClicked, EmojiChooserEventElm.Ui.Init -> commands {
                +EmojiChooserCommandElm.LoadEmojis
            }
        }
    }
}