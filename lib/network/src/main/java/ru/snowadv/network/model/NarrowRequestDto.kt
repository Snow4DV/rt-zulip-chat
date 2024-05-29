package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NarrowRequestDto(
    @SerialName("operator")
    val operator: String,
    @SerialName("operand")
    val operand: String,
) {
    fun toNarrowList(): List<String> {
        return listOf(operator, operand)
    }

    companion object {
        fun ofStreamAndTopic(streamName: String, topicName: String): List<NarrowRequestDto> {
            return listOf(NarrowRequestDto("stream", streamName), NarrowRequestDto("topic", topicName))
        }
        fun ofStream(streamName: String): List<NarrowRequestDto> {
            return listOf(NarrowRequestDto("stream", streamName))
        }
    }


}