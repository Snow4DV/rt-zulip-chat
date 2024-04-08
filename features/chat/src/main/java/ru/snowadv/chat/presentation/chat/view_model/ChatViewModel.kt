package ru.snowadv.chat.presentation.chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.snowadv.chat.data.repository.StubMessageRepository
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.chat.presentation.chat.event.ChatScreenEvent
import ru.snowadv.chat.presentation.chat.event.ChatScreenFragmentEvent
import ru.snowadv.chat.presentation.chat.state.ChatScreenState
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.use_case.AddReactionUseCase
import ru.snowadv.chat.domain.use_case.GetMessagesUseCase
import ru.snowadv.chat.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat.domain.use_case.SendMessageUseCase
import ru.snowadv.chat.presentation.util.mapToAdapterMessagesAndDates
import ru.snowadv.chat.presentation.util.toUiChatMessage
import ru.snowadv.domain.model.Resource
import ru.snowadv.presentation.util.toScreenState

internal class ChatViewModel(
    private val router: ChatRouter,
    private val streamName: String,
    private val topicName: String,
    private val addReactionUseCase: AddReactionUseCase = AddReactionUseCase(),
    private val removeReactionUseCase: RemoveReactionUseCase = RemoveReactionUseCase(),
    private val sendMessageUseCase: SendMessageUseCase = SendMessageUseCase(),
    private val getMessagesUseCase: GetMessagesUseCase = GetMessagesUseCase(),
) : ViewModel() {

    private var messageCollectorJob: Job? = null
    private var changeReactionJob: Job? = null
    private var sendMessageJob: Job? = null

    private val _state = MutableStateFlow(createInitialState())
    val state: Flow<ChatScreenState> = _state
        .onStart { startCollectingMessages() }
        .onCompletion { stopCollectingMessages() }

    private val _eventFlow = MutableSharedFlow<ChatScreenFragmentEvent>(extraBufferCapacity = 1)
    val eventFlow = _eventFlow.asSharedFlow()

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
                startCollectingMessages()
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

    private fun startCollectingMessages() {
        messageCollectorJob?.cancel()
        messageCollectorJob = getMessagesUseCase(streamName, topicName).onEach { messagesRes ->
            _state.update { state ->
                state.copy(
                    screenState = messagesRes.toScreenState(
                        mapper = { messageList ->
                            messageList.mapToAdapterMessagesAndDates(1)
                        },
                        isEmptyChecker = { messageList ->
                            messageList.isEmpty()
                        },
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun stopCollectingMessages() {
        messageCollectorJob?.cancel()
    }
}