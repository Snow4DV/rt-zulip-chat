package ru.snowadv.message_actions_presentation.emoji_chooser.ui.model

import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.utils.EmojiUtils

data class ChatEmoji(
    val name: String,
    val code: String,
    val convertedEmojiString: String,
): DelegateItem {
    override val id: Any
        get() = code
}