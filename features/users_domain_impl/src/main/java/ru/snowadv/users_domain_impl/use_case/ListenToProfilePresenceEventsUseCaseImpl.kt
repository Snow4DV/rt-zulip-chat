package ru.snowadv.users_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventType
import ru.snowadv.users_domain_api.use_case.ListenToProfilePresenceEventsUseCase
import javax.inject.Inject

@Reusable
internal class ListenToProfilePresenceEventsUseCaseImpl @Inject constructor(private val eventRepository: EventRepository): ListenToProfilePresenceEventsUseCase {
    companion object {
        internal val eventTypes =
            setOf(
                EventType.PRESENCE,
            )
    }

    override operator fun invoke(
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