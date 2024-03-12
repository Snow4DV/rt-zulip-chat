package ru.snowadv.app_contacts.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList()
): Parcelable