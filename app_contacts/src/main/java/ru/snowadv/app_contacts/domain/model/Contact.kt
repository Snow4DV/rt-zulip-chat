package ru.snowadv.app_contacts.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList()
): Parcelable