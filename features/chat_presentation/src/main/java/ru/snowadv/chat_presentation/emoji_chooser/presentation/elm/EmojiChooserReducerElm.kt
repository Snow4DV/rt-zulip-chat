package ru.snowadv.chat_presentation.emoji_chooser.presentation.elm

import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class EmojiChooserReducerElm @Inject constructor():
    ScreenDslReducer<EmojiChooserEventElm, EmojiChooserEventElm.Ui, EmojiChooserEventElm.Internal, EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserCommandElm>(
        uiEventClass = EmojiChooserEventElm.Ui::class,
        internalEventClass = EmojiChooserEventElm.Internal::class,
    ) {
    override fun Result.internal(event: EmojiChooserEventElm.Internal) {
        when(event) {
            is EmojiChooserEventElm.Internal.EmojiLoadError -> state {
                copy(screenState = ScreenState.Error(event.error))
            }
            EmojiChooserEventElm.Internal.EmojiLoading -> state {
                copy(screenState = ScreenState.Loading())
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