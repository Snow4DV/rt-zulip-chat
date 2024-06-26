package ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm

import dagger.Reusable
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.util.EmojiChooserMappers.toDomainChatEmoji
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.util.EmojiChooserMappers.toUiChatEmoji
import ru.snowadv.presentation.elm.ElmMapper
import javax.inject.Inject

@Reusable
internal class EmojiChooserElmUiMapper @Inject constructor() :
    ElmMapper<EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserEventElm, EmojiChooserStateUiElm, EmojiChooserEffectUiElm, EmojiChooserEventUiElm> {
    override fun mapState(state: EmojiChooserStateElm): EmojiChooserStateUiElm {
        return EmojiChooserStateUiElm(screenState = state.screenState.map { list -> list.map { it.toUiChatEmoji() } })
    }

    override fun mapEffect(effect: EmojiChooserEffectElm): EmojiChooserEffectUiElm = when(effect) {
        is EmojiChooserEffectElm.CloseWithChosenEmoji -> EmojiChooserEffectUiElm.CloseWithChosenEmoji(
            emoji = effect.emoji.toUiChatEmoji()
        )
    }

    override fun mapUiEvent(uiEvent: EmojiChooserEventUiElm): EmojiChooserEventElm = when(uiEvent) {
        EmojiChooserEventUiElm.Init -> EmojiChooserEventElm.Ui.Init
        is EmojiChooserEventUiElm.OnEmojiChosen -> EmojiChooserEventElm.Ui.OnEmojiChosen(uiEvent.emoji.toDomainChatEmoji())
        EmojiChooserEventUiElm.OnRetryClicked -> EmojiChooserEventElm.Ui.OnRetryClicked
    }

}