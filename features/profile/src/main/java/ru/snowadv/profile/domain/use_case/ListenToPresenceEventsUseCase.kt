package ru.snowadv.profile.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToPresenceEventsUseCase(private val eventRepository: EventRepository) {
    companion object {
        internal val eventTypes =
            listOf(
                EventType.PRESENCE,
            )
    }

    operator fun invoke(
        userId: Long?,
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
    ): Flow<DomainEvent> {
        return eventRepository.listenEvents(
            types = EventType.entries.toSet(),
            narrows = emptySet(),
            delayBeforeObtain = isRestart,
            eventQueueProps = eventQueueProps,
        ).filter { presenceEvent ->
            !(presenceEvent is DomainEvent.PresenceDomainEvent
                    && userId?.let { userId -> presenceEvent.userId != userId } ?: !presenceEvent.currentUser)
        }
    }
}