package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventsDto(
    @SerialName("events")
    val events: List<EventDto>
)

@Serializable
data class EventQueueDto(
    @SerialName("queue_id")
    val queueId: String,
    @SerialName("last_event_id")
    val lastEventId: Long,
)

@Serializable
sealed class EventDto {
    abstract val id: Long
    @Serializable
    @SerialName("message")
    data class MessageEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("message")
        val message: MessageDto
    ): EventDto()

    companion object {
        const val MESSAGE_EVENT_TYPE = "message"
    }
}