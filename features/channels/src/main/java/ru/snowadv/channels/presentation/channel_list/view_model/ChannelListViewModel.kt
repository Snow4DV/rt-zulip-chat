package ru.snowadv.channels.presentation.channel_list.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import ru.snowadv.channels.presentation.channel_list.event.ChannelListEvent
import ru.snowadv.channels.presentation.channel_list.state.ChannelListScreenState
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import kotlin.time.Duration

internal class ChannelListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ChannelListScreenState())
    val state = _state.asStateFlow()

    val searchQuery = state.map { it.searchQuery }

    fun handleEvent(event: ChannelListEvent) {
        when (event) {
            is ChannelListEvent.SearchQueryChanged -> {
                _state.update { oldState ->
                    oldState.copy(
                        searchQuery = event.query
                    )
                }
            }
        }
    }
}