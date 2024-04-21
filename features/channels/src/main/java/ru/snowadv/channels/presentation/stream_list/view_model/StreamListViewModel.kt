package ru.snowadv.channels.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels.domain.use_case.GetTopicsUseCase
import ru.snowadv.channels.domain.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels.presentation.model.ShimmerTopic
import ru.snowadv.model.Resource
import ru.snowadv.channels.presentation.stream_list.event.StreamListEvent
import ru.snowadv.channels.presentation.stream_list.event.StreamListFragmentEvent
import ru.snowadv.channels.presentation.stream_list.state.StreamListScreenState
import ru.snowadv.channels.presentation.util.ChannelsMapper.toUiModel
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.util.toScreenState
import ru.snowadv.presentation.view_model.ViewModelConst

internal class StreamListViewModel(
    private val type: StreamType,
    private val router: ChannelsRouter,
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val listenToStreamEventsUseCase: ListenToStreamEventsUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    searchQueryFlow: StateFlow<String>,
) : ViewModel() {

    private val screenState = MutableStateFlow(StreamListScreenState())

    private var eventListenerJob: Job? = null
    private val eventListenerBag = MutableEventQueueListenerBag()

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
        }.onStart { startStopListenerByScreenState(screenState.value.screenState) }
        .onEach {
            startStopListenerByScreenState(it.screenState)
        }.onCompletion { stopListeningToEvents() }.flowOn(defaultDispatcher)

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

    private fun loadStreams(): Job {
        return getStreamsUseCase(type).onEach { resource ->
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
                            it.loadTopics(streamId, resource.data.map { topic ->
                                topic.toUiModel()
                            })
                        }
                    }
                }
            }.launchIn(this)
        }
    }


    private fun startListeningToEvents() {
        if (eventListenerJob?.isActive == true) return

        eventListenerJob = listenToStreamEventsUseCase(
            bag = eventListenerBag,
            reloadAction = { loadStreams().join() }
        )
            .onStart { screenState.first { it.screenState is ScreenState.Success } }
            .onEach(::handleOnlineEvent)
            .flowOn(defaultDispatcher)
            .launchIn(viewModelScope)
    }

    private fun handleOnlineEvent(event: DomainEvent) {
        viewModelScope.launch {
            when (event) {
                is DomainEvent.MessageDomainEvent -> {
                    event.eventMessage.streamId?.let { streamId ->
                        screenState.update {
                            it.addNewMessage(
                                streamId,
                                event.eventMessage.subject,
                                event.eventMessage.id,
                            )
                        }
                    }
                }

                is DomainEvent.RegisteredNewQueueEvent -> {
                    screenState.update {
                        it.setInitialUnreadMessages(event.streamUnreadMessages?.map { // TODO replace with new event system
                            updateMsgFlags -> updateMsgFlags.toUiModel()
                        } ?: error ("Wrong event queue registered. Check event types in use case"))
                    }
                }

                is DomainEvent.AddReadMessageFlagEvent -> {
                    screenState.update {
                        it.markMessagesAsRead(
                            event.addFlagMessagesIds
                        )
                    }
                }

                is DomainEvent.RemoveReadMessageFlagEvent -> {
                    screenState.update {
                        it.markMessagesAsUnread(event.removeFlagMessages.map {
                            updateFlagMessages -> updateFlagMessages.toUiModel()
                        })


                    }
                }

                is DomainEvent.UserSubscriptionDomainEvent -> {
                    if (type == StreamType.SUBSCRIBED) {
                        event.subscriptions?.let { subscriptions ->
                            when(event.op) {
                                "add" -> screenState.update {
                                    it.addStreams(subscriptions.map { eventStream -> eventStream.toUiModel() })
                                }
                                "remove" -> screenState.update {
                                    it.removeStreams(subscriptions.map { eventStream -> eventStream.toUiModel() })
                                }
                            }
                        }
                    }
                }

                is DomainEvent.StreamDomainEvent -> {
                    if (type == StreamType.ALL) {
                        event.streams?.let { streams ->
                            when (event.op) {
                                "create" -> screenState.update {
                                    it.addStreams(streams.map { stream -> stream.toUiModel() })
                                }
                                "delete" -> screenState.update {
                                    it.removeStreams(streams.map { stream -> stream.toUiModel() })
                                }
                            }
                        }
                    } else if (event.op == "update") {
                        event.streamId?.let { streamId ->
                            event.streamName?.let { streamName ->
                                screenState.update {
                                    it.updateStream(streamId, streamName)
                                }
                            }
                        }
                    }
                }

                else -> Unit // ignored
            }
        }
    }

    private fun stopListeningToEvents() {
        eventListenerJob?.cancel()
    }

    private fun startStopListenerByScreenState(state: ScreenState<*>) {
        if (state is ScreenState.Success || state is ScreenState.Empty) {
            startListeningToEvents()
        } else {
            stopListeningToEvents()
        }
    }


}