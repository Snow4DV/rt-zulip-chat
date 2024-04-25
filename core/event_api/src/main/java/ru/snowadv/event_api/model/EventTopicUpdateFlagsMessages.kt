package ru.snowadv.event_api.model

data class EventTopicUpdateFlagsMessages(
    val topicName: String,
    val unreadMessagesIds: List<Long>,
)