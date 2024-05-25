package ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm

import ru.snowadv.message_actions_presentation.emoji_chooser.ui.model.ChatEmoji

internal sealed interface EmojiChooserEventUiElm {
    data object Init : EmojiChooserEventUiElm
    data class OnEmojiChosen(val emoji: ChatEmoji): EmojiChooserEventUiElm
    data object OnRetryClicked: EmojiChooserEventUiElm
}