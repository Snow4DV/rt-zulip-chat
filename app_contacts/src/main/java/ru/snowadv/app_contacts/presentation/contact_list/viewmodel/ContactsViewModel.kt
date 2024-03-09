package ru.snowadv.app_contacts.presentation.contact_list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactsViewModel: ViewModel() {
    private val _state = MutableStateFlow(ContactsUiState())
    val state = _state.asStateFlow()

    private val _activityEventFlow = MutableSharedFlow<ContactsActivityEvent>()
    val activityEventFlow = _activityEventFlow.asSharedFlow()


    fun event(event: ContactsUiEvent) {
        viewModelScope.launch(Dispatchers.Main) {
            when(event) {
                is ContactsUiEvent.FetchContactsButtonClicked -> {
                    _activityEventFlow.emit(ContactsActivityEvent.AskContactsPermission)
                }
                is ContactsUiEvent.FetchContactsPermissionAllowed -> {
                    _activityEventFlow.emit(ContactsActivityEvent.OpenObtainerActivity)
                }
                is ContactsUiEvent.FetchedNewContacts -> {
                    _state.value = state.value.copy(contacts = event.contacts)
                }
                is ContactsUiEvent.FetchContactsPermissionDenied -> {
                    _activityEventFlow.emit(ContactsActivityEvent.ShowNoPermissionToast)
                }
                is ContactsUiEvent.ObtainCancelledOrFailed -> {
                    _activityEventFlow.emit(ContactsActivityEvent.ShowObtainCancelledToast)
                }
            }
        }
    }
}