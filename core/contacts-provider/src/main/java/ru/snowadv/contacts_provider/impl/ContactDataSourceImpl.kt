package ru.snowadv.contacts_provider.impl

import android.content.Context
import android.provider.ContactsContract
import ru.snowadv.contacts_provider.ContactDataSource
import ru.snowadv.contacts_provider.model.Contact
import java.lang.IllegalArgumentException

class ContactDataSourceImpl(appContext: Context) : ContactDataSource {

    private val context = appContext.applicationContext
    override fun getContacts(): Result<List<Contact>> {
        return try {
            Result.success(getContactsWithPhoneNumbersAndEmails())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getContactsWithPhoneNumbersAndEmails(): List<Contact> {

        val contacts = LinkedHashMap<Long, Contact>()

        context.contentResolver?.query(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DATA1
            ),
            "${ContactsContract.Data.MIMETYPE}=? OR ${ContactsContract.Data.MIMETYPE}=?",
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
            ),
            "${ContactsContract.Contacts.DISPLAY_NAME} ASC, ${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} ASC"
        )?.use { cursor ->
            if (cursor.count == 0) return@use

            val idColumnIndex =
                cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val displayNameColumnIndex =
                cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)
            val mimeTypeColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE)
            val dataColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Data.DATA1)

            while (cursor.moveToNext()) {
                val contactId = cursor.getLong(idColumnIndex)
                val displayName = cursor.getString(displayNameColumnIndex)
                val mimeTypeColumn = cursor.getString(mimeTypeColumnIndex)
                val dataColumn = cursor.getString(dataColumnIndex)
                contacts[contactId] = (contacts[contactId] ?: Contact(contactId, displayName)).let {
                    when (mimeTypeColumn) {
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                            it.copy(phoneNumbers = it.phoneNumbers + dataColumn)
                        }

                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                            it.copy(emails = it.emails + dataColumn)
                        }

                        else -> it
                    }
                }
            }
        } ?: run {
            throw NullPointerException("Couldn't get cursor for table with URI ${ContactsContract.Data.CONTENT_URI}")
        }

        return contacts.values.toList()
    }
}