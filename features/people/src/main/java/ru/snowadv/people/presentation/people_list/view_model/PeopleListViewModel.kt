package ru.snowadv.people.presentation.people_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.people.presentation.people_list.event.PeopleListEvent
import ru.snowadv.people.presentation.people_list.event.PeopleListFragmentEvent
import ru.snowadv.people.presentation.people_list.state.PeopleListScreenState
import ru.snowadv.people.presentation.util.toUiModel
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.presentation.view_model.ViewModelConst

internal class PeopleListViewModel(
    private val router: PeopleRouter,
    private val peopleRepo: PeopleRepository,
) : ViewModel() {

    val searchPublisher = MutableStateFlow("")

    private val _state = MutableStateFlow(PeopleListScreenState())
    @OptIn(FlowPreview::class)
    val state = _state
        .combine(searchPublisher.debounce(ViewModelConst.SEARCH_QUERY_DEBOUNCE_MS)) { state, searchQuery ->
            if (searchQuery.isNotEmpty()) {
                state.filterBySearchQuery(searchQuery)
            } else {
                state
            }
        }
        .flowOn(Dispatchers.Default)

    private val _eventFlow = MutableSharedFlow<PeopleListFragmentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        loadPeople()
    }

    fun handleEvent(event: PeopleListEvent) {
        when(event) {
            is PeopleListEvent.ClickedOnPerson -> {
                router.openProfile(event.userId)
            }

            PeopleListEvent.ClickedOnRetry -> {
                loadPeople()
            }

            PeopleListEvent.ClickedOnSearchIcon -> {
                viewModelScope.launch { _eventFlow.emit(PeopleListFragmentEvent.FocusOnSearchFieldAndOpenKeyboard) }
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