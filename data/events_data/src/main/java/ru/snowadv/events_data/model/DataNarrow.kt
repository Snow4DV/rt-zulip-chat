package ru.snowadv.events_data.model

data class DataNarrow(
    val operator: String,
    val operand: String,
) {
    companion object {
        fun ofStreamAndTopic(streamName: String, topicName: String): List<DataNarrow> {
            return listOf(DataNarrow("stream", streamName), DataNarrow("topic", topicName))
        }
    }
}