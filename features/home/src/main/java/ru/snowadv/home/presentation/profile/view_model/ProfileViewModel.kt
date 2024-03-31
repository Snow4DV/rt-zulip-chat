package ru.snowadv.home.presentation.profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.snowadv.home.data.StubPeopleRepository
import ru.snowadv.home.domain.repository.PeopleRepository
import ru.snowadv.home.presentation.model.Person
import ru.snowadv.home.presentation.navigation.HomeRouter
import ru.snowadv.home.presentation.people_list.event.PeopleListEvent
import ru.snowadv.home.presentation.people_list.state.PeopleListScreenState
import ru.snowadv.home.presentation.profile.event.ProfileEvent
import ru.snowadv.home.presentation.profile.state.ProfileScreenState
import ru.snowadv.home.presentation.util.toUiModel
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.util.toScreenState

internal class ProfileViewModel(
    private val router: HomeRouter,
    private val profileId: Long,
    private val peopleRepo: PeopleRepository = StubPeopleRepository,
    isOwner: Boolean,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileScreenState(isOwner = isOwner))
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
        peopleRepo.getPerson(profileId).onEach {  resource ->
            _state.update { oldState ->
                oldState.copy(
                    screenState = resource.toScreenState(mapper = { person -> person.toUiModel() }),
                )
            }
        }.launchIn(viewModelScope)
    }
}