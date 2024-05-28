package ru.snowadv.chatapp.server.queue

import ru.snowadv.network.model.EventResponseDto

internal data class MockEventQueueData(
    val queueId: String,
    val events: List<EventResponseDto> = emptyList(),
    val lastCollectedId: Long = -1,
) {
    fun hasNewEvent(): Boolean {
        return (events.lastOrNull()?.id ?: -1) > lastCollectedId
    }

    fun markEventsAsViewed(): MockEventQueueData {
        return copy(lastCollectedId = events.last().id)
    }
}