package ru.snowadv.channels.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.channels.data.repository.StubStreamRepository
import ru.snowadv.channels.data.repository.StubTopicRepository
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.channels.presentation.model.ShimmerTopic
import ru.snowadv.domain.model.Resource
import ru.snowadv.channels.domain.model.Stream as DomainStream
import ru.snowadv.channels.presentation.stream_list.event.StreamListEvent
import ru.snowadv.channels.presentation.stream_list.event.StreamListFragmentEvent
import ru.snowadv.channels.presentation.stream_list.state.StreamListScreenState
import ru.snowadv.channels.presentation.util.toUiModel
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.presentation.view_model.ViewModelConst

internal class StreamListViewModel(
    private val type: StreamType,
    private val router: ChannelsRouter,
    private val streamRepo: StreamRepository = StubStreamRepository,
    private val topicRepo: TopicRepository = StubTopicRepository,
    searchQueryFlow: Flow<String>,
) : ViewModel() {

    private val screenState = MutableStateFlow(StreamListScreenState())

    @OptIn(FlowPreview::class)
    val uiState = screenState
        .combine(
            searchQueryFlow.distinctUntilChanged().debounce(ViewModelConst.SEARCH_QUERY_DEBOUNCE_MS)
        ) { screenState, searchQuery ->
            screenState.filterStreamsByQuery(searchQuery)
        }

    private val obtainStreamsAccordingToType get() = getStreamsFunction()

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
                viewModelScope.launch {
                    screenState.value.selectedStream?.name?.let { streamName ->
                        router.openTopic(
                            streamName,
                            event.topicName,
                        )
                    }
                }
            }

            is StreamListEvent.ClickedOnReload -> {
                loadStreams()
            }
        }
    }

    private fun loadStreams() {
        obtainStreamsAccordingToType().onEach { resource ->
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
            getTopicsJob = topicRepo.getTopics(streamId).onEach { resource ->
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


    private fun getStreamsFunction(): () -> Flow<Resource<List<DomainStream>>> {
        return when (type) {
            StreamType.SUBSCRIBED -> streamRepo::getSubscribedStreams
            StreamType.ALL -> streamRepo::getStreams
        }
    }
}