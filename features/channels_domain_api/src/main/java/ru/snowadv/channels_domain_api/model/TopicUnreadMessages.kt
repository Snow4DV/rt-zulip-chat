package ru.snowadv.channels_domain_api.model

data class TopicUnreadMessages(
    val topicName: String,
    val unreadMessagesIds: List<Long>,
)