package ru.snowadv.people_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_api.domain.model.EventQueueProperties
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.events_api.domain.model.EventType
import javax.inject.Inject

@Reusable
internal class ListenToPresenceEventsUseCase @Inject constructor(private val eventRepository: EventRepository) {
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