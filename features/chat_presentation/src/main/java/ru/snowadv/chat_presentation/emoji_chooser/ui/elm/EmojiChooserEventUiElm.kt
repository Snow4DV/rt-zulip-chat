package ru.snowadv.chat_presentation.emoji_chooser.ui.elm

import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji

internal sealed interface EmojiChooserEventUiElm {
    data object Init : EmojiChooserEventUiElm
    data class OnEmojiChosen(val emoji: ChatEmoji): EmojiChooserEventUiElm
    data object OnRetryClicked: EmojiChooserEventUiElm
}