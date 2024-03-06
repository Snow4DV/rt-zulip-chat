package ru.snowadv.contacts_provider.model

data class Contact(
    val id: Long,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList()
)