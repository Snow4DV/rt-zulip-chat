package ru.snowadv.users_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventType
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase
import javax.inject.Inject

@Reusable
internal class ListenToPeoplePresenceEventsUseCaseImpl @Inject constructor(private val eventRepository: EventRepository): ListenToPeoplePresenceEventsUseCase {
    companion object {
        internal val eventTypes = setOf(EventType.PRESENCE)
    }

    override operator fun invoke(
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