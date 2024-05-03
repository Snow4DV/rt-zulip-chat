package ru.snowadv.people_impl.presentation.util

import ru.snowadv.events_data_api.model.DomainEvent
import ru.snowadv.events_data_api.model.EventPresence
import ru.snowadv.people_impl.presentation.model.Person
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListEventElm
import ru.snowadv.people_impl.domain.model.Person as DomainPerson
import ru.snowadv.people_impl.domain.model.Person.Status as DomainPersonStatus

internal object PeopleMappers {
    fun DomainPersonStatus.toUiModel(): Person.Status {
        return Person.Status.entries[ordinal]
    }

    fun DomainPerson.toUiModel(): Person {
        return Person(id, fullName, email, avatarUrl, status.toUiModel())
    }

    fun EventPresence.toUiModel(): Person.Status {
        return Person.Status.entries[ordinal]
    }

    fun DomainEvent.toUpdateQueueDataElmEvent(): PeopleListEventElm.Internal.ServerEvent.EventQueueUpdated {
        return PeopleListEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = queueId,
            eventId = id,
        )
    }

    fun DomainEvent.PresenceDomainEvent.toElmEvent(): PeopleListEventElm.Internal.ServerEvent.PresenceUpdated {
        return PeopleListEventElm.Internal.ServerEvent.PresenceUpdated(
            queueId = queueId,
            newStatus = presence.toUiModel(),
            eventId = id,
            userId = userId,
        )
    }

    fun DomainEvent.FailedFetchingQueueEvent.toElmEvent(): PeopleListEventElm.Internal.ServerEvent.EventQueueFailed {
        return PeopleListEventElm.Internal.ServerEvent.EventQueueFailed(
            queueId = queueId,
            eventId = id,
            recreateQueue = isQueueBad
        )
    }

    fun DomainEvent.RegisteredNewQueueEvent.toElmEvent(): PeopleListEventElm.Internal.ServerEvent.EventQueueRegistered {
        return PeopleListEventElm.Internal.ServerEvent.EventQueueRegistered(
            queueId = queueId,
            timeoutSeconds = timeoutSeconds,
            eventId = id,
        )
    }
}