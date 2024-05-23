package ru.snowadv.channels_domain_api.model

data class Stream(
    val id: Long,
    val name: String,
    val subscribed: Boolean,
    val subscribing: Boolean,
    val color: String?,
    val unreadMessagesCount: Int = 0,
)