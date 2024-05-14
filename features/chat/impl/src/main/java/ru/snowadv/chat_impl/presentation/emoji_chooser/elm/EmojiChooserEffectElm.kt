package ru.snowadv.chat_impl.presentation.emoji_chooser.elm

import ru.snowadv.chat_impl.presentation.model.ChatEmoji

internal sealed interface EmojiChooserEffectElm {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserEffectElm
}