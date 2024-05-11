package ru.snowadv.chat_domain_api.model

data class ChatPaginatedMessages(
    val messages: List<ChatMessage>,
    val foundAnchor: Boolean,
    val foundOldest: Boolean,
    val foundNewest: Boolean,
)