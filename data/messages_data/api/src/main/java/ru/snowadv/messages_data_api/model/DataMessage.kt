package ru.snowadv.messages_data_api.model

import java.time.ZonedDateTime

data class DataMessage(
    val id: Long,
    val content: String,
    val sentAt: ZonedDateTime,
    val senderId: Long,
    val senderName: String,
    val senderAvatarUrl: String?,
    val reactions: List<DataReaction>,
    val owner: Boolean,
)