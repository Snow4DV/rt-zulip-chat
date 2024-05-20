package ru.snowadv.people_presentation.ui.util

import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventPresence
import ru.snowadv.people_presentation.ui.model.UiPerson
import ru.snowadv.people_presentation.presentation.elm.PeopleListEventElm
import ru.snowadv.users_domain_api.model.Person

internal object PeopleMappers {
    fun Person.Status.toUiModel(): UiPerson.Status {
        return UiPerson.Status.valueOf(toString())
    }

    fun Person.toUiModel(): UiPerson {
        return UiPerson(
            id = id,
            fullName = fullName,
            email = email,
            avatarUrl = avatarUrl,
            status = status.toUiModel(),
        )
    }

    fun EventPresence.toDomainModel(): Person.Status {
        return Person.Status.entries.first { it.apiName == apiName }
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
            newStatus = presence.toDomainModel(),
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