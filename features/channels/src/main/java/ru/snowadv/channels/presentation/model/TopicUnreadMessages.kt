package ru.snowadv.channels.presentation.model

data class TopicUnreadMessages(
    val topicName: String,
    val unreadMessagesIds: List<Long>,
)