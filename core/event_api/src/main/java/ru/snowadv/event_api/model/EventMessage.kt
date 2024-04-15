package ru.snowadv.event_api.model

import java.time.ZonedDateTime

data class EventMessage(
    val id: Long,
    val content: String,
    val sentAt: ZonedDateTime,
    val senderId: Long,
    val senderFullName: String,
    val avatarUrl: String? = null,
    val reactions: List<EventReaction>,
    val owner: Boolean,
)