package ru.snowadv.events_data.model

import ru.snowadv.network.model.EventDto

enum class DataEventType(val apiName: String) {
    MESSAGES(EventDto.MESSAGE_EVENT_TYPE),
}