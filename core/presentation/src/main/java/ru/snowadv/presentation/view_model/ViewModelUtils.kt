package ru.snowadv.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T> ViewModel.emitToStateFlowInViewModelScope(stateFlow: MutableStateFlow<T>, newValue: T) {
    viewModelScope.launch { stateFlow.emit(newValue) }
}