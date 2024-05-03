package ru.snowadv.profile_impl.presentation.util

import ru.snowadv.events_api.domain.model.DomainEvent
import ru.snowadv.events_api.domain.model.EventPresence
import ru.snowadv.profile_impl.presentation.model.Person
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileEventElm
import ru.snowadv.profile_impl.domain.model.Person as DomainPerson
import ru.snowadv.profile_impl.domain.model.Person.Status as DomainPersonStatus

internal object ProfileMappers {
    fun DomainPersonStatus.toUiModel(): Person.Status {
        return Person.Status.entries[ordinal]
    }

    fun DomainPerson.toUiModel(): Person {
        return Person(id, fullName, email, avatarUrl, status.toUiModel())
    }

    fun EventPresence.toUiModel(): Person.Status {
        return Person.Status.entries[ordinal]
    }

    fun DomainEvent.RegisteredNewQueueEvent.toElmEvent(): ProfileEventElm.Internal.ServerEvent.EventQueueRegistered {
        return ProfileEventElm.Internal.ServerEvent.EventQueueRegistered(
            queueId = queueId,
            timeoutSeconds = timeoutSeconds,
            eventId = id,
        )
    }

    fun DomainEvent.toUpdateQueueDataElmEvent(): ProfileEventElm.Internal.ServerEvent.EventQueueUpdated {
        return ProfileEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = queueId,
            eventId = id,
        )
    }

    fun DomainEvent.PresenceDomainEvent.toElmEvent(): ProfileEventElm.Internal.ServerEvent.PresenceUpdated {
        return ProfileEventElm.Internal.ServerEvent.PresenceUpdated(
            queueId = queueId,
            newStatus = presence.toUiModel(),
            eventId = id,
        )
    }

    fun DomainEvent.FailedFetchingQueueEvent.toElmEvent(): ProfileEventElm.Internal.ServerEvent.EventQueueFailed {
        return ProfileEventElm.Internal.ServerEvent.EventQueueFailed(
            queueId = queueId,
            eventId = id,
            recreateQueue = isQueueBad
        )
    }
}