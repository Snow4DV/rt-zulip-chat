package ru.snowadv.channels.presentation.channel_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.channels.presentation.channel_list.event.ChannelListEvent
import ru.snowadv.channels.presentation.channel_list.event.ChannelListFragmentEvent

internal class ChannelListViewModel : ViewModel() {



    private val _eventFlow = MutableSharedFlow<ChannelListFragmentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    val searchQueryPublisher = MutableStateFlow("")

    fun handleEvent(event: ChannelListEvent) {
        when (event) {
            ChannelListEvent.SearchIconClicked -> {
                viewModelScope.launch { _eventFlow.emit(ChannelListFragmentEvent.ShowKeyboardAndFocusOnTextField) }
            }
        }
    }
}