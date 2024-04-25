package ru.snowadv.chat.presentation.model

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