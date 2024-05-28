package ru.snowadv.channels_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.model.EventType
import javax.inject.Inject

@Reusable
internal class ListenToStreamEventsUseCaseImpl @Inject constructor(
    private val eventRepository: EventRepository,
): ListenToStreamEventsUseCase {
    companion object {
        internal val eventTypes =
            setOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.MESSAGE, EventType.UPDATE_MESSAGE_FLAGS,
                EventType.SUBSCRIPTION, EventType.STREAM,
            )
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