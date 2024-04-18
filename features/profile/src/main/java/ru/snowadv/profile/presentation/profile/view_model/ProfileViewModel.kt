package ru.snowadv.profile.presentation.profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.model.Resource
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.profile.presentation.profile.event.ProfileEvent
import ru.snowadv.profile.presentation.profile.state.ProfileScreenState
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.use_case.GetProfileUseCase
import ru.snowadv.profile.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.profile.presentation.util.ProfileMappers.toUiModel

internal class ProfileViewModel(
    private val router: ProfileRouter,
    private val profileId: Long?,
    private val getProfileUseCase: GetProfileUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState(isOwner = profileId == null))
    val state = _state.onStart {
        startStopListener(_state.value.screenState)
    }.onEach {
        startStopListener(it.screenState)
    }.onCompletion {
        startStopListener(_state.value.screenState)
    }

    private val eventBag = MutableEventQueueListenerBag()

    private var listenerJob: Job? = null

    init {
        loadProfile()
    }

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.ClickedOnRetry -> loadProfile()
            ProfileEvent.ClickedOnLogout -> {
                // TODO: Remove auth from auth repository & navigate to logout screen
            }
            ProfileEvent.ClickedOnBack -> router.goBack()
        }
    }

    private fun loadProfile(): Job {
        return getProfileUseCase(profileId).onEach { resource ->
            _state.update { oldState ->
                oldState.copy(
                    screenState = resource.toScreenState(mapper = { person -> person.toUiModel() }),
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun startStopListener(state: ScreenState<*>) {
        when(state) {
            is ScreenState.Empty -> stopListener()
            is ScreenState.Error -> stopListener()
            is ScreenState.Loading -> stopListener()
            is ScreenState.Success -> startListener()
        }
    }

    private fun startListener() {
        if (listenerJob?.isActive == true) return

        listenerJob = listenToPresenceEventsUseCase(eventBag, profileId) { loadProfile().join() }.onEach { event ->
            _state.update {state ->
                state.updatePresence(event.presence.toUiModel())
            }
        }.launchIn(viewModelScope)
    }

    private fun stopListener() {
        listenerJob?.cancel()
        listenerJob = null
    }
}