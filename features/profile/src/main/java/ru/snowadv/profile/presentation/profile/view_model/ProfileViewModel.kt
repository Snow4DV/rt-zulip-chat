package ru.snowadv.profile.presentation.profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.snowadv.model.Resource
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.profile.presentation.profile.event.ProfileEvent
import ru.snowadv.profile.presentation.profile.state.ProfileScreenState
import ru.snowadv.profile.presentation.util.toUiModel
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.profile.domain.navigation.ProfileRouter

internal class ProfileViewModel(
    private val router: ProfileRouter,
    private val profileId: Long?,
    private val profileRepo: ProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState(isOwner = profileId == null))
    val state = _state.asStateFlow()

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

    private fun loadProfile() {
        (profileId?.let { profileRepo.getPerson(it) } ?: profileRepo.getCurrentPerson()).onEach { resource ->
            _state.update { oldState ->
                oldState.copy(
                    screenState = resource.toScreenState(mapper = { person -> person.toUiModel() }),
                )
            }
        }.launchIn(viewModelScope)
    }
}