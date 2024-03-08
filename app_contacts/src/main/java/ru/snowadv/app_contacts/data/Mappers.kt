package ru.snowadv.app_contacts.data

import ru.snowadv.contacts_provider.model.Contact as DataContact
import ru.snowadv.app_contacts.domain.model.Contact


fun DataContact.toUiContact(): Contact {
    return Contact(
        id = id,
        name = name,
        phoneNumbers = phoneNumbers,
        emails = emails
    )
}