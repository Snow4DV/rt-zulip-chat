package ru.snowadv.channels_impl.presentation.model

data class TopicUnreadMessages(
    val topicName: String,
    val unreadMessagesIds: List<Long>,
)