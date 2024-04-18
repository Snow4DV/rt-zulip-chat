package ru.snowadv.channels.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.channels.di.ChannelsGraph
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels.domain.use_case.GetTopicsUseCase
import ru.snowadv.channels.presentation.model.ShimmerTopic
import ru.snowadv.model.Resource
import ru.snowadv.channels.presentation.stream_list.event.StreamListEvent
import ru.snowadv.channels.presentation.stream_list.event.StreamListFragmentEvent
import ru.snowadv.channels.presentation.stream_list.state.StreamListScreenState
import ru.snowadv.channels.presentation.util.toUiModel
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.presentation.view_model.ViewModelConst

internal class StreamListViewModel(
    private val type: StreamType,
    private val router: ChannelsRouter,
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    searchQueryFlow: StateFlow<String>,
) : ViewModel() {

    private val screenState = MutableStateFlow(StreamListScreenState())

    @OptIn(FlowPreview::class)
    val uiState = screenState
        .combine(
            searchQueryFlow.debounce(ViewModelConst.SEARCH_QUERY_DEBOUNCE_MS)
        ) { screenState, searchQuery ->
            if (searchQuery.isNotEmpty()) {
                screenState.filterStreamsByQuery(searchQuery)
            } else {
                screenState
            }
        }.flowOn(Dispatchers.Default)

    private val _eventFlow = MutableSharedFlow<StreamListFragmentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getTopicsJob: Job? = null

    init {
        loadStreams()
    }

    fun handleEvent(event: StreamListEvent) {
        when (event) {
            is StreamListEvent.ClickedOnStream -> {
                loadOrHideTopics(event.streamId)
            }

            is StreamListEvent.ClickedOnTopic -> {
                screenState.value.selectedStream?.name?.let { streamName ->
                    router.openTopic(
                        streamName,
                        event.topicName,
                    )
                }
            }

            is StreamListEvent.ClickedOnReload -> {
                loadStreams()
            }
        }
    }

    private fun loadStreams() {
        getStreamsUseCase(type).onEach { resource ->
            screenState.update { state ->
                state.copy(
                    screenState = resource.toScreenState(
                        mapper = { streamList ->
                            streamList.map { stream -> stream.toUiModel() }
                        },
                        isEmptyChecker = {
                            it.isEmpty()
                        },
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun loadOrHideTopics(streamId: Long) {
        getTopicsJob?.cancel()
        viewModelScope.launch {
            if (screenState.value.selectedStream?.id == streamId) {
                screenState.update {
                    it.hideTopics()
                }
                return@launch
            }
            getTopicsJob = getTopicsUseCase(streamId).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        screenState.update { it.hideTopics() }
                        _eventFlow.emit(
                            StreamListFragmentEvent.ShowInternetErrorWithRetry(
                                retryAction = {
                                    loadOrHideTopics(streamId)
                                }
                            )
                        )
                    }

                    is Resource.Loading -> {
                        screenState.update {
                            it.loadTopics(
                                streamId,
                                ShimmerTopic.generateShimmers(streamId)
                            )
                        }
                    }

                    is Resource.Success -> {
                        screenState.update {
                            it.loadTopics(streamId, resource.data.mapIndexed { index, topic ->
                                topic.toUiModel(
                                    index
                                )
                            })
                        }
                    }
                }
            }.launchIn(this)
        }
    }


}