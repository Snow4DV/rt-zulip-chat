package ru.snowadv.channels_domain_api.model

data class Topic(
    val uniqueId: String,
    val name: String,
    val streamId: Long,
    val unreadMessagesCount: Int = 0,
)