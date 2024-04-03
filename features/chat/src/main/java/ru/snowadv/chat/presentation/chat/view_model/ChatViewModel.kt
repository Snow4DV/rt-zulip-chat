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
import ru.snowadv.chat.presentation.util.toUiChatMessage
import ru.snowadv.domain.model.Resource
import ru.snowadv.presentation.util.toScreenState

internal class ChatViewModel(
    private val repository: MessageRepository = StubMessageRepository, // remove after adding DI
    private val router: ChatRouter,
    private val streamName: String,
    private val topicName: String,
) : ViewModel() {

    private var messageCollectorJob: Job? = null

    private val _state = MutableStateFlow(createInitialState())
    val state: Flow<ChatScreenState> = _state
        .onStart { startCollectingMessages() }
        .onCompletion { stopCollectingMessages() }
    private val _fragmentEventFlow = MutableSharedFlow<ChatScreenFragmentEvent>()
    val fragmentEventFlow = _fragmentEventFlow.asSharedFlow()

    private fun createInitialState(): ChatScreenState {
        return ChatScreenState(
            stream = streamName, topic = topicName
        )
    }


    fun handleEvent(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.TextFieldMessageChanged -> {
                if (event.text != _state.value.messageField) {
                    _state.update {  state ->
                        state.copy(
                            messageField = event.text
                        )
                    }
                }
            }

            is ChatScreenEvent.SendMessageAddAttachmentButtonClicked -> {
                if (_state.value.messageField.isEmpty()) {
                    viewModelScope.launch { _fragmentEventFlow.emit(ChatScreenFragmentEvent.ExplainNotImplemented) }
                } else {
                    sendMessage(_state.value.messageField)
                }
            }

            is ChatScreenEvent.AddReactionClicked -> {
                viewModelScope.launch {
                    _fragmentEventFlow.emit(
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
                    _fragmentEventFlow.emit(
                        ChatScreenFragmentEvent.OpenMessageActionsChooser(
                            event.messageId,
                            event.userId
                        )
                    )
                }
            }
        }
    }

    private fun sendMessage(text: String) {

        repository.sendMessage(
            streamName = _state.value.stream, topicName = _state.value.topic, text = text
        ).onEach(::processCompletableResource)
            .onCompletion {
                if (it == null) clearMessageFieldAndScrollToTheEnd()
            }
            .launchIn(viewModelScope)
    }

    private fun clearMessageFieldAndScrollToTheEnd() {
        viewModelScope.launch {
            _fragmentEventFlow.emit(ChatScreenFragmentEvent.ScrollRecyclerToTheEnd)
            _state.value = _state.value.copy(
                messageField = ""
            )
        }
    }

    private fun addReaction(messageId: Long, reactionName: String) {
        repository.addReaction(messageId, reactionName)
            .onEach {
                processCompletableResource(
                    it,
                    ChatScreenFragmentEvent.ExplainReactionAlreadyExists
                )
            }
            .launchIn(viewModelScope)
    }

    private fun removeReaction(messageId: Long, reactionName: String) {
        repository.removeReaction(messageId, reactionName).onEach(::processCompletableResource)
            .launchIn(viewModelScope)
    }

    private fun processCompletableResource(
        resource: Resource<Unit>,
        errorChatScreenFragmentEvent: ChatScreenFragmentEvent = ChatScreenFragmentEvent.ExplainError,
    ) {
        when (resource) {
            is Resource.Loading -> {
                _state.value = _state.value.copy(
                    actionInProcess = true,
                )
            }

            is Resource.Success -> {
                _state.value = _state.value.copy(
                    actionInProcess = false
                )
            }

            is Resource.Error -> {
                _state.value = _state.value.copy(
                    actionInProcess = false
                )
                viewModelScope.launch {
                    _fragmentEventFlow.emit(errorChatScreenFragmentEvent)
                }
            }
        }
    }

    private fun startCollectingMessages() {
        messageCollectorJob?.cancel()
        messageCollectorJob = repository.getMessages(streamName, topicName).onEach { messagesRes ->
            _state.update { state ->
                state.copy(
                    screenState = messagesRes.toScreenState(
                        mapper = { messageList ->
                            messageList.map { message -> message.toUiChatMessage(1) }
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