package ru.snowadv.channels_api.domain.model

data class Topic(
    val uniqueId: String,
    val name: String,
    val streamId: Long,
)