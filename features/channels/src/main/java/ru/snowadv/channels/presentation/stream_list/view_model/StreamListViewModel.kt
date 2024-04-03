package ru.snowadv.channels.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import ru.snowadv.channels.data.repository.StubStreamRepository
import ru.snowadv.channels.data.repository.StubTopicRepository
import ru.snowadv.channels.domain.model.Stream
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.domain.model.Resource
import ru.snowadv.channels.presentation.channel_list.event.ChannelListEvent
import ru.snowadv.channels.presentation.channel_list.view_model.ChannelListViewModel
import ru.snowadv.channels.presentation.stream_list.event.StreamListEvent
import ru.snowadv.channels.presentation.stream_list.event.StreamListFragmentEvent
import ru.snowadv.channels.presentation.stream_list.state.StreamListScreenState
import ru.snowadv.channels.presentation.util.toUiModel
import ru.snowadv.channels.presentation.util.toUiModelListWithPositions
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.utils.hasAnyCoroutinesRunning
import ru.snowadv.utils.hasMoreThanOneCoroutineRunning

internal class StreamListViewModel(
    private val type: StreamType,
    private val router: ChannelsRouter,
    private val streamRepo: StreamRepository = StubStreamRepository,
    private val topicRepo: TopicRepository = StubTopicRepository,
    private val searchQueryFlow: Flow<String>,
) : ViewModel() {

    private val obtainStreamsAccordingToType get() = getStreamsFunction()

    private val _state = MutableStateFlow(StreamListScreenState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<StreamListFragmentEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getTopicsJob: Job? = null

    init {
        observeSearchQuery()
    }

    fun handleEvent(event: StreamListEvent) {
        when (event) {
            is StreamListEvent.ClickedOnStream -> {
                loadOrHideTopics(event.streamId)
            }

            is StreamListEvent.ClickedOnTopic -> {
                state.value.selectedStream?.name?.let { streamName ->
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

    init {
        loadStreams()
    }

    private fun loadStreams() {
        obtainStreamsAccordingToType().onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _state.update {
                        _state.value.copy(
                            screenState = ScreenState.Error(resource.throwable)
                        )
                    }
                }

                Resource.Loading -> {
                    _state.update {
                        _state.value.copy(
                            screenState = ScreenState.Loading
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        _state.value.copy(
                            screenState = if (resource.data.isNotEmpty()) {
                                ScreenState.Success(resource.data.map { it.toUiModel() })
                            } else {
                                ScreenState.Empty
                            }
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadOrHideTopics(streamId: Long) {
        if (state.value.selectedStream?.id == streamId) {
            _state.value = state.value.hideTopics()
            return
        }
        getTopicsJob?.cancel()
        getTopicsJob = topicRepo.getTopics(streamId).onEach { resource ->
            when (resource) {
                is Resource.Error -> {
                    _state.update {
                        _state.value.copy(
                            actionInProgress = false
                        )
                    }
                    viewModelScope.launch {
                        _eventFlow.emit(
                            StreamListFragmentEvent.ShowInternetErrorWithRetry(
                            retryAction = {
                                loadOrHideTopics(streamId)
                            }
                        ))
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        _state.value.copy(
                            actionInProgress = true
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.loadTopics(streamId, resource.data.toUiModelListWithPositions()).copy(
                            actionInProgress = false
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun getStreamsFunction(): () -> Flow<Resource<List<Stream>>> {
        return when (type) {
            StreamType.SUBSCRIBED -> streamRepo::getSubscribedStreams
            StreamType.ALL -> streamRepo::getStreams
        }
    }

    private fun observeSearchQuery() {
        searchQueryFlow.onEach { query ->
            _state.update { state ->
                state.copy(
                    searchQuery = query,
                )
            }
        }.launchIn(viewModelScope)
    }
}