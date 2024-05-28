package ru.snowadv.events_api.model

data class EventNarrow(
    val operator: String,
    val operand: String,
) {
    companion object {
        fun ofStreamAndTopic(streamName: String, topicName: String): List<EventNarrow> {
            return listOf(EventNarrow("stream", streamName), EventNarrow("topic", topicName))
        }
    }
}