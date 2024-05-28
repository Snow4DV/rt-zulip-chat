package ru.snowadv.chat_domain_api.model

import java.time.ZonedDateTime

data class ChatMessage(
    val id: Long,
    val content: String,
    val sentAt: ZonedDateTime,
    val senderId: Long,
    val senderName: String,
    val senderAvatarUrl: String?,
    val reactions: List<ChatReaction>,
    val owner: Boolean,
    val topic: String,
    val isRead: Boolean,
)