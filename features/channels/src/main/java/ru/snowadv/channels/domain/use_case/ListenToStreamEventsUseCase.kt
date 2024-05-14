package ru.snowadv.channels.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventNarrow
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToStreamEventsUseCase(
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