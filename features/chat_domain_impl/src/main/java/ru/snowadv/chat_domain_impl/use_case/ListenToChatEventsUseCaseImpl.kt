package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventNarrow
import ru.snowadv.events_api.model.EventType
import javax.inject.Inject

@Reusable
internal class ListenToChatEventsUseCaseImpl @Inject constructor(private val eventRepository: EventRepository) :
    ListenToChatEventsUseCase {
    companion object {
        internal val eventTypes =
            setOf(
                EventType.REALM, EventType.HEARTBEAT, EventType.PRESENCE, EventType.MESSAGE,
                EventType.DELETE_MESSAGE, EventType.UPDATE_MESSAGE, EventType.REACTION
            )
    }

    override operator fun invoke(
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