package ru.snowadv.channels.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToStreamEventsUseCase(
    private val eventRepository: EventRepository,
) {
    companion object {
        internal val eventTypes =
            listOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.MESSAGE, EventType.UPDATE_MESSAGE_FLAGS,
            )
    }

    operator fun invoke(bag: MutableEventQueueListenerBag, reloadAction: suspend () -> Unit): Flow<DomainEvent> {
        return eventRepository.listenForEvents(bag, eventTypes, emptyList(), reloadAction)
    }
}