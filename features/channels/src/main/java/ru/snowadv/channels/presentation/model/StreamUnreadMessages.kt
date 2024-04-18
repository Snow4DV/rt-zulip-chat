package ru.snowadv.channels.presentation.model

data class StreamUnreadMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<TopicUnreadMessages>,
)