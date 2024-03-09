package ru.snowadv.app_contacts.presentation.contact_list.viewmodel

sealed class ContactsActivityEvent {
    data object AskContactsPermission: ContactsActivityEvent()
    data object OpenObtainerActivity: ContactsActivityEvent()
    data object ShowObtainCancelledToast: ContactsActivityEvent()
    data object ShowNoPermissionToast: ContactsActivityEvent()
}