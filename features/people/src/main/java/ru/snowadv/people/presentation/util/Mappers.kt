package ru.snowadv.people.presentation.util

import ru.snowadv.people.presentation.model.Person
import ru.snowadv.people.domain.model.Person as DomainPerson
import ru.snowadv.people.domain.model.Person.Status as DomainPersonStatus

internal fun DomainPersonStatus.toUiModel(): Person.Status {
    return ru.snowadv.people.presentation.model.Person.Status.entries[ordinal]
}

internal fun DomainPerson.toUiModel(): Person {
    return Person(id, fullName, email, avatarUrl, status.toUiModel())
}