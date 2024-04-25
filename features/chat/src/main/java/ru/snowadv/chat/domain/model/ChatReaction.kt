package ru.snowadv.chat.domain.model

data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
)