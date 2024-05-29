package ru.snowadv.chat_presentation.chat.ui.model

import ru.snowadv.presentation.adapter.DelegateItem

data class ChatEmoji(
    val name: String,
    val code: String,
    val convertedEmojiString: String,
): DelegateItem {
    override val id: Any
        get() = code
}