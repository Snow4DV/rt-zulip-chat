package ru.snowadv.chat_impl.domain.model

data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
)