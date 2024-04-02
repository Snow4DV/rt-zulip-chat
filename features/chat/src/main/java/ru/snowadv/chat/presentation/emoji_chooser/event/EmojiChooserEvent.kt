package ru.snowadv.chat.presentation.emoji_chooser.event

import ru.snowadv.chat.presentation.model.ChatEmoji

sealed class EmojiChooserEvent {
    class OnEmojiChosen(val emoji: ChatEmoji): EmojiChooserEvent()
    data object OnRetryClicked: EmojiChooserEvent()
}