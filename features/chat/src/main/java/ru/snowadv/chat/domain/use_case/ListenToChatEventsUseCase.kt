package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventNarrow
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToChatEventsUseCase(private val eventRepository: EventRepository) {
    companion object {
        internal val eventTypes =
            listOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.PRESENCE, EventType.MESSAGE,
                EventType.DELETE_MESSAGE, EventType.UPDATE_MESSAGE, EventType.REACTION
            )
    }

    operator fun invoke(
        bag: MutableEventQueueListenerBag,
        streamName: String,
        topicName: String,
        reloadAction: suspend () -> Unit
    ): Flow<DomainEvent> {
        return eventRepository.listenForEvents(
            bag = bag,
            types = eventTypes,
            narrows = listOf(EventNarrow("stream", streamName), EventNarrow("topic", topicName)),
            reloadAction = reloadAction,
        )
    }
}