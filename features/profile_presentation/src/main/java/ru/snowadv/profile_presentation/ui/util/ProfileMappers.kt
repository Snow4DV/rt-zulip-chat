package ru.snowadv.profile_presentation.ui.util

import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventPresence
import ru.snowadv.profile_presentation.ui.model.UiPerson
import ru.snowadv.profile_presentation.presentation.elm.ProfileEventElm
import ru.snowadv.users_domain_api.model.Person
import ru.snowadv.users_domain_api.model.Person as DomainPerson
import ru.snowadv.users_domain_api.model.Person.Status as DomainPersonStatus

internal object ProfileMappers {
    fun DomainPersonStatus.toUiModel(): UiPerson.Status {
        return UiPerson.Status.entries[ordinal]
    }

    fun DomainPerson.toUiModel(): UiPerson {
        return UiPerson(id, fullName, email, avatarUrl, status.toUiModel())
    }

    fun EventPresence.toDomainModel(): DomainPerson.Status {
        return Person.Status.entries.first { it.apiName == apiName }
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
            newStatus = presence.toDomainModel(),
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