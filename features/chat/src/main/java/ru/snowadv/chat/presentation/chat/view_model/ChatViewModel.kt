package ru.snowadv.chat.presentation.chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.chat.domain.model.ChatPaginatedMessages
import ru.snowadv.chat.presentation.chat.event.ChatScreenEvent
import ru.snowadv.chat.presentation.chat.event.ChatScreenFragmentEvent
import ru.snowadv.chat.presentation.chat.state.ChatScreenState
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.use_case.AddReactionUseCase
import ru.snowadv.chat.domain.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat.domain.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat.domain.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat.domain.use_case.SendMessageUseCase
import ru.snowadv.chat.presentation.model.ChatPaginationStatus
import ru.snowadv.chat.presentation.util.mapToAdapterMessagesAndDates
import ru.snowadv.chat.presentation.util.mapToUiAdapterMessagesAndDates
import ru.snowadv.chat.presentation.util.toUiChatEmoji
import ru.snowadv.chat.presentation.util.toUiChatMessage
import ru.snowadv.event_api.helper.MutableEventQueueListenerBag
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.util.toScreenState

internal class ChatViewModel(
    private val router: ChatRouter,
    private val streamName: String,
    private val topicName: String,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getMessagesUseCase: GetCurrentMessagesUseCase,
    private val listenToChatEventsUseCase: ListenToChatEventsUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
) : ViewModel() {

    private var eventListenerJob: Job? = null
    private val eventListenerBag = MutableEventQueueListenerBag()

    private var changeReactionJob: Job? = null
    private var sendMessageJob: Job? = null

    private var paginationJob: Job? = null

    private val _state = MutableStateFlow(createInitialState())
    val state: Flow<ChatScreenState> = _state
        .onStart { startStopListenerByScreenState(_state.value.screenState) }
        .onEach {
            startStopListenerByScreenState(it.screenState)
        }
        .onCompletion { stopListeningToEvents() }

    private val _eventFlow = MutableSharedFlow<ChatScreenFragmentEvent>(extraBufferCapacity = 1)
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getInitMessages()
    }

    private fun createInitialState(): ChatScreenState {
        return ChatScreenState(
            stream = streamName, topic = topicName
        )
    }

    fun handleEvent(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.SendMessageAddAttachmentButtonClicked -> {
                if (_state.value.messageField.isEmpty()) {
                    viewModelScope.launch { _eventFlow.emit(ChatScreenFragmentEvent.ExplainNotImplemented) }
                } else {
                    sendMessage(_state.value.messageField)
                }
            }

            is ChatScreenEvent.AddReactionClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        ChatScreenFragmentEvent.OpenReactionChooser(
                            event.messageId
                        )
                    )
                }
            }

            is ChatScreenEvent.AddChosenReaction -> {
                addReaction(event.messageId, event.reactionName)
            }

            is ChatScreenEvent.RemoveReaction -> {
                removeReaction(event.messageId, event.reactionName)
            }

            is ChatScreenEvent.GoBackClicked -> {
                router.goBack()
            }

            is ChatScreenEvent.GoToProfileClicked -> {
                router.openProfile(event.profileId)
            }

            ChatScreenEvent.ReloadClicked -> {
                viewModelScope.launch { getInitMessages() }
            }

            is ChatScreenEvent.MessageLongClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        ChatScreenFragmentEvent.OpenMessageActionsChooser(
                            event.messageId,
                            event.userId
                        )
                    )
                }
            }

            is ChatScreenEvent.MessageFieldChanged -> {
                _state.update {
                    it.copy(
                        messageField = event.text,
                        actionButtonType = if (event.text.isEmpty()) {
                            ChatScreenState.ActionButtonType.ADD_ATTACHMENT
                        } else {
                            ChatScreenState.ActionButtonType.SEND_MESSAGE
                        }
                    )
                }
            }

            ChatScreenEvent.PaginationLoadMore -> {
                loadMoreMessages()
            }
            ChatScreenEvent.ScrolledToTop -> {
                //loadMoreMessages()
            }
        }
    }

    private fun sendMessage(text: String) {
        sendMessageJob?.cancel()
        sendMessageJob = sendMessageUseCase(
            streamName = _state.value.stream, topicName = _state.value.topic, text = text
        ).onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            sendingMessage = true
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            sendingMessage = false,
                            messageField = "",
                        )
                    }
                    _eventFlow.tryEmit(ChatScreenFragmentEvent.ScrollRecyclerToTheEnd)
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            sendingMessage = false
                        )
                    }
                    _eventFlow.tryEmit(ChatScreenFragmentEvent.ShowInternetErrorWithRetry {
                        sendMessage(text)
                    })
                }
            }
        }
            .launchIn(viewModelScope)
    }

    private fun addReaction(messageId: Long, reactionName: String) {
        changeReactionJob?.cancel()
        changeReactionJob = addReactionUseCase(messageId, reactionName)
            .onEach {
                processReactionResource(
                    it,
                    ChatScreenEvent.AddChosenReaction(messageId, reactionName)
                )
            }
            .launchIn(viewModelScope)
    }

    private fun removeReaction(messageId: Long, reactionName: String) {
        changeReactionJob?.cancel()
        changeReactionJob = removeReactionUseCase(messageId, reactionName).onEach {
            processReactionResource(it, ChatScreenEvent.RemoveReaction(messageId, reactionName))
        }
            .launchIn(viewModelScope)
    }

    private fun processReactionResource(
        resource: Resource<Unit>,
        retryEvent: ChatScreenEvent
    ) {
        when (resource) {
            is Resource.Loading -> {
                _state.update {
                    it.copy(
                        changingReaction = true
                    )
                }
            }

            is Resource.Success -> {
                _state.update {
                    it.copy(
                        changingReaction = false
                    )
                }
            }

            is Resource.Error -> {
                _state.update {
                    it.copy(
                        changingReaction = false
                    )
                }
                _eventFlow.tryEmit(ChatScreenFragmentEvent.ShowInternetErrorWithRetry {
                    handleEvent(
                        retryEvent
                    )
                })
            }
        }
    }

    private fun getInitMessages(): Job {
        paginationJob?.cancel()
        return getMessagesUseCase(streamName, topicName).onEach { messagesRes ->
            _state.update { state ->
                state.copy(
                    screenState = messagesRes.map { it.messages }.toScreenState(
                        mapper = { messageList ->
                            messageList.mapToUiAdapterMessagesAndDates()
                        },
                        isEmptyChecker = { messageList ->
                            messageList.isEmpty()
                        },
                    ),
                    messages = messagesRes.getDataOrNull()?.messages?.map { it.toUiChatMessage() }
                        ?: emptyList(),
                    paginationStatus = messagesRes.getDataOrNull()?.let { data ->
                        when {
                            data.foundOldest -> ChatPaginationStatus.LoadedAll
                            else -> ChatPaginationStatus.HasMore
                        }
                    } ?: ChatPaginationStatus.None
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun loadMoreMessages() {
        if (paginationJob?.isActive == true) return

        with(_state.value) {
            if (screenState !is ScreenState.Success || paginationStatus !is ChatPaginationStatus.HasMore) {
                return
            }
        }

        val firstMessageId: Long? = _state.value.firstLoadedMessageId
        val includeAnchor = firstMessageId == null

        paginationJob = loadMoreMessagesUseCase(streamName , topicName, firstMessageId, includeAnchor)
            .onEach(::processPaginationMessagesResource)
            .flowOn(Dispatchers.Default).launchIn(viewModelScope)
    }

    private fun processPaginationMessagesResource(res: Resource<ChatPaginatedMessages>) {
        when(res) {
            is Resource.Error -> {
                _state.update {
                    it.copy(
                        paginationStatus = ChatPaginationStatus.Error
                    )
                }
            }
            is Resource.Loading -> {
                _state.update {
                    it.copy(
                        paginationStatus = ChatPaginationStatus.Loading
                    )
                }
            }
            is Resource.Success -> {
                _state.update { state ->
                    val messagesList = buildList {
                        addAll(res.data.messages.map { it.toUiChatMessage() })
                        addAll(state.messages)
                    }
                    state.copy(
                        paginationStatus = if (res.data.foundOldest) {
                            ChatPaginationStatus.LoadedAll
                        } else {
                            ChatPaginationStatus.HasMore
                        },
                        messages = messagesList,
                        screenState = res.map { messagesList }.toScreenState(
                            mapper = { messageList ->
                                messageList.mapToAdapterMessagesAndDates()
                            },
                            isEmptyChecker = { messageList ->
                                messageList.isEmpty()
                            },
                        ),
                    )
                }
            }
        }
    }

    private fun startListeningToEvents() {
        if (eventListenerJob?.isActive == true) return

        eventListenerJob = listenToChatEventsUseCase(
            bag = eventListenerBag,
            streamName = streamName,
            topicName = topicName,
            reloadAction = { getInitMessages().join() }
        )
            .onStart {_state.first { it.screenState is ScreenState.Success } }
            .onEach(::handleOnlineEvent)
            .launchIn(viewModelScope)
    }

    private fun handleOnlineEvent(event: DomainEvent) {
        viewModelScope.launch {
            when (event) {
                is DomainEvent.MessageDomainEvent -> {
                    _state.update {
                        it.addMessage(event.eventMessage.toUiChatMessage())
                    }
                }

                is DomainEvent.DeleteMessageDomainEvent -> {
                    _state.update {
                        it.removeMessage(event.messageId)
                    }
                }

                is DomainEvent.UpdateMessageDomainEvent -> {
                    _state.update {
                        event.content?.let { content -> it.updateMessage(event.messageId, content) }
                            ?: it
                    }
                }

                is DomainEvent.ReactionDomainEvent -> {
                    when (event.op) {
                        "add" -> {
                            _state.update {
                                it.addReaction(
                                    event.toUiChatEmoji(),
                                    event.messageId,
                                    event.currentUserReaction
                                )
                            }
                        }

                        "remove" -> {
                            _state.update {
                                it.removeReaction(
                                    event.toUiChatEmoji(),
                                    event.messageId,
                                    event.currentUserReaction
                                )
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