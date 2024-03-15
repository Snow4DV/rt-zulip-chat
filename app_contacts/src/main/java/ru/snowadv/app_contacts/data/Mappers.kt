package ru.snowadv.app_contacts.data

import ru.snowadv.contacts_provider.model.Contact as DataContact
import ru.snowadv.app_contacts.domain.model.Contact as DomainContact


internal fun DataContact.toDomainContact(): DomainContact {
    return DomainContact(
        id = id,
        name = name,
        phoneNumbers = phoneNumbers,
        emails = emails
    )
}