package ru.snowadv.people.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToPresenceEventsUseCase(private val eventRepository: EventRepository) {
    companion object {
        internal val eventTypes =
            listOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.PRESENCE
            )
    }

    operator fun invoke(
        bag: MutableEventQueueListenerBag,
        reloadAction: suspend () -> Unit
    ): Flow<DomainEvent.PresenceDomainEvent> {
        return eventRepository.listenForEvents(
            bag = bag,
            types = eventTypes,
            narrows = emptyList(),
            reloadAction = reloadAction,
        ).filterIsInstance<DomainEvent.PresenceDomainEvent>()
    }
}