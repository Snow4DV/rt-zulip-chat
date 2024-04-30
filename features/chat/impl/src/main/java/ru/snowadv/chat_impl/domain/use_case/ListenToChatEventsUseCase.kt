package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.events_data_api.model.DomainEvent
import ru.snowadv.events_data_api.model.EventNarrow
import ru.snowadv.events_data_api.model.EventType
import javax.inject.Inject

@Reusable
internal class ListenToChatEventsUseCase @Inject constructor(private val eventRepository: EventRepository) {
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
        streamName: String,
        topicName: String,
    ): Flow<DomainEvent> {
        return eventRepository.listenEvents(
            types = eventTypes,
            narrows = setOf(EventNarrow("stream", streamName), EventNarrow("topic", topicName)),
            delayBeforeObtain = isRestart,
            eventQueueProps = eventQueueProps,
        )
    }
}