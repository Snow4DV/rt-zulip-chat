package ru.snowadv.events_data_api.model

data class EventStreamUpdateFlagsMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<EventTopicUpdateFlagsMessages>,
)