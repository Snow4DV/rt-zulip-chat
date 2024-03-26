package ru.snowadv.chat.presentation.emoji_chooser.state

import ru.snowadv.chat.presentation.model.ChatEmoji

internal data class EmojiChooserState(
    val status: ChooserScreenStatus = ChooserScreenStatus.LOADING,
    val emojis: List<ChatEmoji> = emptyList(),
)

enum class ChooserScreenStatus {
    LOADING,
    ERROR,
    OK,
}