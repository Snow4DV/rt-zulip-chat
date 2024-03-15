package ru.snowadv.app_contacts.presentation.contact_list.viewmodel

import ru.snowadv.app_contacts.presentation.model.Contact


internal sealed class ContactsUiEvent {
    data object FetchContactsButtonClicked: ContactsUiEvent()
    data object FetchContactsPermissionAllowed: ContactsUiEvent()
    data object FetchContactsPermissionDenied: ContactsUiEvent()
    data object ObtainCancelledOrFailed: ContactsUiEvent()
    data class FetchedNewContacts(val contacts: List<Contact>): ContactsUiEvent()
}