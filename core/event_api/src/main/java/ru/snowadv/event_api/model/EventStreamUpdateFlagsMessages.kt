package ru.snowadv.event_api.model

data class EventStreamUpdateFlagsMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<EventTopicUpdateFlagsMessages>,
)