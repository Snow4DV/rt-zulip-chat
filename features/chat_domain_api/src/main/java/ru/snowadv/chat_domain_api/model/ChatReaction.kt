package ru.snowadv.chat_domain_api.model

data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
)