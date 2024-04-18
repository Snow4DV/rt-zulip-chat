package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponseDto(
    @SerialName("topics")
    val topics: List<TopicResponseDto>,
)

@Serializable
data class TopicResponseDto(
    @SerialName("name")
    val name: String,
)