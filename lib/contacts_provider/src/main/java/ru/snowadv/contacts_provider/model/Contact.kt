package ru.snowadv.contacts_provider.model

import android.os.Parcelable

data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList()
)