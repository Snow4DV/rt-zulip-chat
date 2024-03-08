package ru.snowadv.app_contacts.data

import android.content.Context
import androidx.annotation.RequiresPermission
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.domain.model.RequestResult
import ru.snowadv.app_contacts.domain.repository.ContactRepository
import ru.snowadv.contacts_provider.ContactDataSource
class ContactRepositoryImpl(
    private val contactDataSource: ContactDataSource
): ContactRepository {
    @RequiresPermission("android.permission.READ_CONTACTS")
    override suspend fun getContacts(): RequestResult<List<Contact>> {
        runCatching {
            return RequestResult.Success(contactDataSource.getContacts().map { it.toUiContact() })
        }
        return RequestResult.Error()
    }
}