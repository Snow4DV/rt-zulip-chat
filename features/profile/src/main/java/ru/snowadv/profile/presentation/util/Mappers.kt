package ru.snowadv.profile.presentation.util

import ru.snowadv.event_api.model.EventPresence
import ru.snowadv.profile.presentation.model.Person
import ru.snowadv.profile.domain.model.Person as DomainPerson
import ru.snowadv.profile.domain.model.Person.Status as DomainPersonStatus

internal fun DomainPersonStatus.toUiModel(): Person.Status {
    return Person.Status.entries[ordinal]
}

internal fun DomainPerson.toUiModel(): Person {
    return Person(id, fullName, email, avatarUrl, status.toUiModel())
}

internal fun EventPresence.toUiModel(): Person.Status {
    return Person.Status.entries[ordinal]
}