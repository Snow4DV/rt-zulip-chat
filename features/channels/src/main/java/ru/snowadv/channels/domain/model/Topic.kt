package ru.snowadv.channels.domain.model

data class Topic(
    val uniqueId: String,
    val name: String,
    val streamId: Long,
)