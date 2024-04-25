package ru.snowadv.people.presentation.people_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.use_case.GetPeopleUseCase
import ru.snowadv.people.domain.use_case.ListenToPresenceEventsUseCase
import ru.snowadv.people.presentation.people_list.event.PeopleListEvent
import ru.snowadv.people.presentation.people_list.event.PeopleListFragmentEvent
import ru.snowadv.people.presentation.people_list.state.PeopleListScreenState
import ru.snowadv.people.presentation.util.PeopleMappers.toUiModel
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.presentation.view_model.ViewModelConst

internal class PeopleListViewModel(
    private val router: PeopleRouter,
    private val getPeopleUseCase: GetPeopleUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
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
        .onStart { startStopListener(_state.value.screenState) }
        .onEach { startStopListener(it.screenState) }
        .onCompletion { startStopListener(_state.value.screenState) }
        .flowOn(defaultDispatcher)

    private val _eventFlow = MutableSharedFlow<PeopleListFragmentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val eventBag = MutableEventQueueListenerBag()
    private var listenerJob: Job? = null


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
        getPeopleUseCase().onEach {  resource ->
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

        listenerJob = listenToPresenceEventsUseCase(eventBag) { _state.first { it.screenState.getCurrentData() != null } }.onEach { event ->
            _state.update { state ->
                state.updateStatus(event.userId, event.presence.toUiModel())
            }
        }.launchIn(viewModelScope)
    }

    private fun stopListener() {
        listenerJob?.cancel()
        listenerJob = null
    }


}