package ru.snowadv.channels_impl.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.model.EventType
import javax.inject.Inject

internal class ListenToStreamEventsUseCase @Inject constructor(
    private val eventRepository: EventRepository,
) {
    companion object {
        internal val eventTypes =
            setOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.MESSAGE, EventType.UPDATE_MESSAGE_FLAGS,
                EventType.SUBSCRIPTION, EventType.STREAM,
            )
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