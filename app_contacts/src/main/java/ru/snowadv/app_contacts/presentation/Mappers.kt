package ru.snowadv.app_contacts.presentation

import ru.snowadv.app_contacts.presentation.model.Contact
import ru.snowadv.app_contacts.domain.model.Contact as DomainContact

internal fun DomainContact.toUiContact(): Contact {
    return Contact(id, name, phoneNumbers, emails)
}