package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsDto(
    @SerialName("topics")
    val topics: List<TopicDto>,
)

@Serializable
data class TopicDto(
    @SerialName("name")
    val name: String,
)