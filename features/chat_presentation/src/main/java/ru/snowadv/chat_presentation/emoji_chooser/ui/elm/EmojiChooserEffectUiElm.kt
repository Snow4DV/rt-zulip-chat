package ru.snowadv.chat_presentation.emoji_chooser.ui.elm

import ru.snowadv.chat_presentation.common.ui.model.ChatEmoji

internal sealed interface EmojiChooserEffectUiElm {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserEffectUiElm
}