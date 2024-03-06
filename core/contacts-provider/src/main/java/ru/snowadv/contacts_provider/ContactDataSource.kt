package ru.snowadv.contacts_provider

import android.content.Context
import androidx.annotation.RequiresPermission
import ru.snowadv.contacts_provider.impl.CoroutineContactDataSourceImpl
import ru.snowadv.contacts_provider.model.Contact

interface ContactDataSource {
    @RequiresPermission("android.permission.READ_CONTACTS")
    suspend fun getContacts(context: Context): List<Contact>
}

/** Shortcut for default implementation */
fun ContactDataSource(): ContactDataSource = CoroutineContactDataSourceImpl()