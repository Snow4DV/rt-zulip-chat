package ru.snowadv.events_api.model

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
    val type: String,
    val streamId: Long? = null, // will not be present if it is DM message
    val subject: String, // will be ""/empty string for DMs
    val flags: List<String>,
)