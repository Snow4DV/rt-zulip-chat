package ru.snowadv.chat.domain.model

data class ChatPaginatedMessages(
    val messages: List<ChatMessage>,
    val foundAnchor: Boolean,
    val foundOldest: Boolean,
    val foundNewest: Boolean,
)