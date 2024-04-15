package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class NarrowDto(
    @SerialName("operator")
    val operator: String,
    @SerialName("operand")
    val operand: String,
) {
    fun toNarrowList(): List<String> {
        return listOf(operator, operand)
    }

    companion object {
        fun ofStreamAndTopic(streamName: String, topicName: String): List<NarrowDto> {
            return listOf(NarrowDto("stream", streamName), NarrowDto("topic", topicName))
        }
    }


}