package ru.snowadv.events_api.domain.model

data class EventTopicUpdateFlagsMessages(
    val topicName: String,
    val unreadMessagesIds: List<Long>,
)