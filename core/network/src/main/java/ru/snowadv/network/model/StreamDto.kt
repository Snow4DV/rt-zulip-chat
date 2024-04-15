package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllStreamsDto(
    @SerialName("streams")
    val streams: List<StreamDto>,
)

@Serializable
data class SubscribedStreamsDto(
    @SerialName("subscriptions")
    val subscriptions: List<StreamDto>,
)

@Serializable
data class StreamDto(
    @SerialName("stream_id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("color")
    val color: String? = null,
)