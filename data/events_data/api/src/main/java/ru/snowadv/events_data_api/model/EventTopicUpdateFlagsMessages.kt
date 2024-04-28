package ru.snowadv.events_data_api.model

data class EventTopicUpdateFlagsMessages(
    val topicName: String,
    val unreadMessagesIds: List<Long>,
)