package ru.snowadv.chat.domain.model

data class ChatReaction(
    val name: String,
    val emojiCode: Int,
    val count: Int,
    val userReacted: Boolean,
)