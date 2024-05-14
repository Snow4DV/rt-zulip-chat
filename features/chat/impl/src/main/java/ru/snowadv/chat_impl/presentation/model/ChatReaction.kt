package ru.snowadv.chat_impl.presentation.model

data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
    val emojiString: String,
)