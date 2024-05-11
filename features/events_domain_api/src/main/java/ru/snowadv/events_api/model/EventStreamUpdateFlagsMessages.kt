package ru.snowadv.events_api.model

data class EventStreamUpdateFlagsMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<EventTopicUpdateFlagsMessages>,
)