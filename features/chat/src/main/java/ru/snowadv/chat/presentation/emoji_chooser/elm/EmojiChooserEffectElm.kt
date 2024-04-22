package ru.snowadv.chat.presentation.emoji_chooser.elm

import ru.snowadv.chat.presentation.model.ChatEmoji

sealed interface EmojiChooserEffectElm {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserEffectElm
}