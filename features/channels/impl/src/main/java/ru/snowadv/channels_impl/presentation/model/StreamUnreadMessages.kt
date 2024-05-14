package ru.snowadv.channels_impl.presentation.model

data class StreamUnreadMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<TopicUnreadMessages>,
)