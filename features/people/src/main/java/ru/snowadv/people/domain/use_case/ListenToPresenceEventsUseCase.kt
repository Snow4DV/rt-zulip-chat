package ru.snowadv.people.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToPresenceEventsUseCase(private val eventRepository: EventRepository) {
    companion object {
        internal val eventTypes = setOf(EventType.PRESENCE)
    }

    operator fun invoke(
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
    ): Flow<DomainEvent> {
        return eventRepository.listenEvents(
            types = eventTypes,
            narrows = emptySet(),
            delayBeforeObtain = isRestart,
            eventQueueProps = eventQueueProps,
        )
    }
}