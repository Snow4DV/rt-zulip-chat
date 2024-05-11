package ru.snowadv.chat_presentation.emoji_chooser.elm

import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji

internal sealed interface EmojiChooserEffectElm {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserEffectElm
}