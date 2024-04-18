package ru.snowadv.profile.presentation.util

import ru.snowadv.event_api.model.EventPresence
import ru.snowadv.profile.presentation.model.Person
import ru.snowadv.profile.domain.model.Person as DomainPerson
import ru.snowadv.profile.domain.model.Person.Status as DomainPersonStatus

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
}