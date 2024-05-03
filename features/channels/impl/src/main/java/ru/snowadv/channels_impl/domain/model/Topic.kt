package ru.snowadv.channels_impl.domain.model

data class Topic(
    val uniqueId: String,
    val name: String,
    val streamId: Long,
)