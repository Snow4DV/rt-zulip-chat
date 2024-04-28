package ru.snowadv.event_api.helper

import ru.snowadv.events_data_api.model.EventSenderType

interface EventInfoHolder {
    val queueId: String?
    val eventId: Long
    val senderType get() = EventSenderType.SERVER_SIDE
}