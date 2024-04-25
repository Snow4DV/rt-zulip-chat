package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllStreamsResponseDto(
    @SerialName("streams")
    val streams: List<StreamResponseDto>,
)

@Serializable
data class SubscribedStreamsResponseDto(
    @SerialName("subscriptions")
    val subscriptions: List<StreamResponseDto>,
)

@Serializable
data class StreamResponseDto(
    @SerialName("stream_id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("color")
    val color: String? = null,
)