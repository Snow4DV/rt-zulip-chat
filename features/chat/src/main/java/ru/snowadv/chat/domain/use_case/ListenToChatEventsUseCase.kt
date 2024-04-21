package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventNarrow
import ru.snowadv.event_api.model.EventType
import ru.snowadv.event_api.repository.EventRepository

internal class ListenToChatEventsUseCase(private val eventRepository: EventRepository) {
    companion object {
        internal val eventTypes =
            setOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.PRESENCE, EventType.MESSAGE,
                EventType.DELETE_MESSAGE, EventType.UPDATE_MESSAGE, EventType.REACTION
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