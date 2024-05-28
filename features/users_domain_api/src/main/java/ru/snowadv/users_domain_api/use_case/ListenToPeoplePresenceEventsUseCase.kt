package ru.snowadv.users_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.events_api.model.EventType
import ru.snowadv.events_api.repository.EventRepository

interface ListenToPeoplePresenceEventsUseCase  {
    operator fun invoke(
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
    ): Flow<DomainEvent>
}