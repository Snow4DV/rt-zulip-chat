package ru.snowadv.app_contacts.domain.repository

import ru.snowadv.app_contacts.domain.model.Contact

internal interface ContactRepository {
    suspend fun getContacts(): Result<List<Contact>>
}