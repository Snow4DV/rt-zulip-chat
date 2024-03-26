package ru.snowadv.chat.presentation.emoji_chooser.event

import ru.snowadv.chat.presentation.model.ChatEmoji

sealed class EmojiChooserFragmentEvent {
    data class CloseWithChosenEmoji(val emoji: ChatEmoji): EmojiChooserFragmentEvent()
}