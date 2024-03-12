package ru.snowadv.app_contacts.data

import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.domain.repository.ContactRepository
import ru.snowadv.contacts_provider.ContactDataSource
import kotlin.coroutines.coroutineContext

internal class ContactRepositoryImpl(
    private val contactDataSource: ContactDataSource,
    private val ioDispatcher: CoroutineDispatcher
): ContactRepository {
    @RequiresPermission("android.permission.READ_CONTACTS")
    override suspend fun getContacts(): Result<List<Contact>> = withContext(ioDispatcher + coroutineContext) {
        contactDataSource.getContacts().map { contactList -> contactList.map { it.toDomainContact() } }
    }
}