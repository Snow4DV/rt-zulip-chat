package ru.snowadv.people.presentation.people_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.snowadv.people.data.repository.StubPeopleRepository
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.people.presentation.people_list.event.PeopleListEvent
import ru.snowadv.people.presentation.people_list.state.PeopleListScreenState
import ru.snowadv.people.presentation.util.toUiModel
import ru.snowadv.presentation.util.toScreenState

internal class PeopleListViewModel(
    private val router: PeopleRouter,
    private val peopleRepo: PeopleRepository = StubPeopleRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(PeopleListScreenState())
    val state = _state.asStateFlow()

    init {
        loadPeople()
    }

    fun handleEvent(event: PeopleListEvent) {
        when(event) {
            is PeopleListEvent.ChangedSearchQuery -> {
                _state.update {
                    it.copy(
                        searchQuery = event.newQuery
                    )
                }
            }
            is PeopleListEvent.ClickedOnPerson -> {
                router.openProfile(event.userId)
            }

            PeopleListEvent.ClickedOnRetry -> {
                loadPeople()
            }
        }
    }

    private fun loadPeople() {
        peopleRepo.getPeople().onEach {  resource ->
            _state.update {  screenState ->
                screenState.copy(
                    screenState = resource.toScreenState(
                        mapper = { people ->
                            people.map { it.toUiModel() }
                        },
                        isEmptyChecker = { people ->
                            people.isEmpty()
                        }
                    )
                )
            }
        }.launchIn(viewModelScope)
    }
}