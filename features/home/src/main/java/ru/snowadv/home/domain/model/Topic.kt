package ru.snowadv.home.domain.model

internal data class Topic(
    val uniqueId: String, // ID should be generated using stream's ID
    val name: String,
    val streamId: Long,
)