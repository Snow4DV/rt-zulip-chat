package ru.snowadv.chat_api.domain.model

data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
)