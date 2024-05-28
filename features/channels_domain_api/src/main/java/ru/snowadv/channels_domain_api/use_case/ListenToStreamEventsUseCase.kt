package ru.snowadv.channels_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource

interface ListenToStreamEventsUseCase {
    operator fun invoke(isRestart: Boolean, eventQueueProps: EventQueueProperties?): Flow<DomainEvent>
}