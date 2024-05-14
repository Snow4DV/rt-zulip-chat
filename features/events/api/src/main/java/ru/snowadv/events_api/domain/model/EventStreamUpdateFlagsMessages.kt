package ru.snowadv.events_api.domain.model

data class EventStreamUpdateFlagsMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<EventTopicUpdateFlagsMessages>,
)