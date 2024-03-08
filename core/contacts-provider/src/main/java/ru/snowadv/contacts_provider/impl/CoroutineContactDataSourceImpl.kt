package ru.snowadv.contacts_provider.impl

import android.content.Context
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.snowadv.contacts_provider.ContactDataSource
import ru.snowadv.contacts_provider.model.Contact

class CoroutineContactDataSourceImpl(appContext: Context): ContactDataSource {

    private val context = appContext.applicationContext
    override suspend fun getContacts(): List<Contact> = coroutineScope {
        with(Dispatchers.IO) {
            val contactsDeferred = async { getEmptyContacts() }
            val contactNumbersDeferred = async { getContactNumbers() }
            val contactEmailsDeferred = async { getContactEmails() }

            val contactNumbers = contactNumbersDeferred.await()
            val contactEmails = contactEmailsDeferred.await()

            contactsDeferred.await().map {
                it.copy(
                    phoneNumbers = contactNumbers[it.id] ?: emptyList(),
                    emails = contactEmails[it.id] ?: emptyList()
                )
            }
        }
    }

    /** Returns contacts with no phone numbers/emails ordered by DISPLAY_NAME */
    private fun getEmptyContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        context.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME),
            null,
            null,
            "${ContactsContract.Contacts.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            if(cursor.count == 0) return@use

            val idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                contacts.add(Contact(
                    id = cursor.getLong(idColumnIndex),
                    name = cursor.getString(nameColumnIndex)
                ))
            }
        }

        return contacts
    }

    private fun getContactNumbers(): Map<Long, List<String>> {
        val contactNumbers = HashMap<Long, MutableList<String>>()
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER),
            null,
            null,
            null
        )?.use { cursor ->
            if(cursor.count == 0) return@use

            val idColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val contactId = cursor.getLong(idColumnIndex)

                contactNumbers[contactId]?.add(cursor.getString(phoneNumberColumnIndex))
                    ?: run {
                        contactNumbers[contactId] = mutableListOf(cursor.getString(phoneNumberColumnIndex))
                    }
            }
        }

        return contactNumbers
    }

    private fun getContactEmails(): Map<Long, List<String>> {
        val contactEmails = HashMap<Long, MutableList<String>>()
        context.contentResolver?.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.CONTACT_ID, ContactsContract.CommonDataKinds.Email.ADDRESS),
            null,
            null,
            null
        )?.use { cursor ->
            if(cursor.count == 0) return@use

            val idColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
            val emailColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)

            while (cursor.moveToNext()) {
                val contactId = cursor.getLong(idColumnIndex)

                contactEmails[contactId]?.add(cursor.getString(emailColumnIndex))
                    ?: run {
                        contactEmails[contactId] = mutableListOf(cursor.getString(emailColumnIndex))
                    }
            }
        }

        return contactEmails
    }
}