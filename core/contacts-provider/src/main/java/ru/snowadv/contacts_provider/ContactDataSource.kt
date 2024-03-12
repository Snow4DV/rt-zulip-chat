package ru.snowadv.contacts_provider

import androidx.annotation.RequiresPermission
import ru.snowadv.contacts_provider.model.Contact

interface ContactDataSource {
    @RequiresPermission("android.permission.READ_CONTACTS")
    fun getContacts(): Result<List<Contact>>
}