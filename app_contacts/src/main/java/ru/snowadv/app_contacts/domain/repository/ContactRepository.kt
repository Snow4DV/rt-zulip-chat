package ru.snowadv.app_contacts.domain.repository

import android.content.Context
import androidx.annotation.RequiresPermission
import ru.snowadv.app_contacts.domain.model.Contact
import ru.snowadv.app_contacts.domain.model.RequestResult

interface ContactRepository {
    suspend fun getContacts(): RequestResult<List<Contact>>
}