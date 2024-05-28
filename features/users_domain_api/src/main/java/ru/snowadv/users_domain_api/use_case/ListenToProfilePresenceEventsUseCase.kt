package ru.snowadv.users_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties

interface ListenToProfilePresenceEventsUseCase  {
    operator fun invoke(
        userId: Long?,
        isRestart: Boolean,
        eventQueueProps: EventQueueProperties?,
    ): Flow<DomainEvent>
}