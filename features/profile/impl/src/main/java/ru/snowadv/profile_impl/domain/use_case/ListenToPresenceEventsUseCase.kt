package ru.snowadv.profile_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import ru.snowadv.events_api.domain.model.EventQueueProperties
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.events_api.domain.model.EventType
import javax.inject.Inject

@Reusable
internal class ListenToPresenceEventsUseCase @Inject constructor(private val eventRepository: EventRepository) {
    companion object {
        internal val eventTypes =
            setOf(
                EventType.PRESENCE,
            )
    }

    operator fun invoke(
        userId: Long?,
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
    ): Flow<DomainEvent> {
        return eventRepository.listenEvents(
            types = eventTypes,
            narrows = emptySet(),
            delayBeforeObtain = isRestart,
            eventQueueProps = eventQueueProps,
        ).filter { presenceEvent ->
            !(presenceEvent is DomainEvent.PresenceDomainEvent
                    && userId?.let { userId -> presenceEvent.userId != userId } ?: !presenceEvent.currentUser)
        }
    }
}