package ru.snowadv.events_api.model

interface EventInfoHolder {
    val queueId: String?
    val eventId: Long
    val senderType get() = EventSenderType.SERVER_SIDE
}