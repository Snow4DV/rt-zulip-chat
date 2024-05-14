package ru.snowadv.events_api.domain.model

data class EventQueueProperties(
    val queueId: String,
    val timeoutSeconds: Int,
    val lastEventId: Long,
)