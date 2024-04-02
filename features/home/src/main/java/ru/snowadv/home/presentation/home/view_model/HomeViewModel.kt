package ru.snowadv.home.presentation.home.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.snowadv.home.presentation.home.event.HomeScreenEvent
import ru.snowadv.home.presentation.home.state.HomeScreenState
import ru.snowadv.home.presentation.local_navigation.HomeScreen

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    fun handleEvent(event: HomeScreenEvent) {
        when(event) {
            is HomeScreenEvent.OnBottomTabSelected -> {
                _state.update { oldState ->
                    oldState.copy(
                        currentScreen = event.screen
                    )
                }
            }
        }
    }
}