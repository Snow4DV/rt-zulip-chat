package ru.snowadv.app_contacts.domain.model


internal data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList()
)