package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDetailsResponseDto(
    @SerialName("type")
    val type: String,
    @SerialName("stream_id")
    val streamId: Long? = null, // will only be present if type is stream
    @SerialName("topic")
    val topic: String? = null, // will only be present if type is stream
)