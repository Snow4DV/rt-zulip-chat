package ru.snowadv.app_contacts.presentation.contact_list.viewmodel

import ru.snowadv.app_contacts.presentation.model.Contact

internal data class ContactsUiState(
    val contacts: List<Contact> = emptyList()
)