package ru.snowadv.home.presentation.channel_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.snowadv.home.presentation.channel_list.event.ChannelListEvent
import ru.snowadv.home.presentation.channel_list.state.ChannelListScreenState
import ru.snowadv.home.presentation.navigation.HomeRouter

internal class ChannelListSharedViewModel(
    private val router: HomeRouter
) : ViewModel() {

    private val _state = MutableStateFlow(ChannelListScreenState())
    val state = _state.asStateFlow()

    val searchQuery = state.map { it.searchQuery }

    fun handleEvent(event: ChannelListEvent) {
        when (event) {
            is ChannelListEvent.SearchQueryChanged -> {
                _state.update {
                    state.value.copy(
                        searchQuery = event.query
                    )
                }
            }

            is ChannelListEvent.ClickedOnTopic -> {
                router.openTopic(event.streamId, event.topicName)
            }
        }
    }
}