package ru.snowadv.event_api.helper

data class EventQueueProperties(
    val queueId: String,
    val timeoutSeconds: Int,
    val lastEventId: Long,
)