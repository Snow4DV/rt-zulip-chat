package ru.snowadv.chat.presentation.model

data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
    val emojiString: String,
)