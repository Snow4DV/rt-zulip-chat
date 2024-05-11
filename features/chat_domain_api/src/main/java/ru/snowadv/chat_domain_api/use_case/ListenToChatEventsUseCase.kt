package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties

interface ListenToChatEventsUseCase {
    operator fun invoke(
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
        streamName: String,
        topicName: String,
    ): Flow<DomainEvent>
}