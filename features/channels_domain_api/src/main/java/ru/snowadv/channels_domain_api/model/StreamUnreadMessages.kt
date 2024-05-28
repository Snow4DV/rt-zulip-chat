package ru.snowadv.channels_domain_api.model

data class StreamUnreadMessages(
    val streamId: Long,
    val topicsUnreadMessages: List<TopicUnreadMessages>,
)