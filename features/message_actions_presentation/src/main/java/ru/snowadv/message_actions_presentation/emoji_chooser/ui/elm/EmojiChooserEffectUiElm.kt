package ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm

import ru.snowadv.message_actions_presentation.emoji_chooser.ui.model.ChatEmoji


internal sealed interface EmojiChooserEffectUiElm {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserEffectUiElm
}