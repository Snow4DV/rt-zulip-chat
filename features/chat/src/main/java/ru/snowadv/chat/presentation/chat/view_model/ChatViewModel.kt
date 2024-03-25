package ru.snowadv.chat.presentation.chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.snowadv.chat.data.StubMessageRepository
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.chat.presentation.chat.event.ChatScreenEvent
import ru.snowadv.chat.presentation.chat.event.ChatScreenFragmentEvent
import ru.snowadv.chat.presentation.chat.state.ChatScreenState
import ru.snowadv.chat.presentation.util.mapToAdapterMessagesAndDates
import ru.snowadv.domain.model.Resource

internal class ChatViewModel(
    private val repository: MessageRepository = StubMessageRepository() // remove after adding DI
) : ViewModel() {

    private val _state = MutableStateFlow(createDefaultState())
    val state: StateFlow<ChatScreenState> = _state

    private val _fragmentEventFlow = MutableSharedFlow<ChatScreenFragmentEvent>()
    val fragmentEventFlow = _fragmentEventFlow.asSharedFlow()

    init {
        repository.getMessages()
            .onEach(::processMessagesListResource)
            .launchIn(viewModelScope) // TODO: NOT LIFECYCLE AWARE, FIX! DONE FOR TESTING ONLY
    }

    private fun processMessagesListResource(res: Resource<List<ChatMessage>>) {
        when (res) {
            is Resource.Success -> {
                _state.value = state.value.copy(
                    messagesAndDates = res.data.mapToAdapterMessagesAndDates(1)
                )
            }
            else -> {
                // TODO: Restart flow here
            }
        }
    }

    private fun createDefaultState(): ChatScreenState {
        return ChatScreenState(
            stream = "#general", topic = "#default", loading = false
        ) // Hardcore names at this point
    }


    fun event(event: ChatScreenEvent) {
        when (event) {
            is ChatScreenEvent.TextFieldMessageChanged -> {
                _state.value = state.value.copy(
                    messageField = event.text
                )
            }

            is ChatScreenEvent.SendButtonClicked -> {
                sendMessage(event.currentMessageFieldText)
            }

            is ChatScreenEvent.AddAttachmentButtonClicked -> {
                viewModelScope.launch { _fragmentEventFlow.emit(ChatScreenFragmentEvent.ExplainError) }
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
                //addReaction(event.messageId, event.reaction)
            }

            is ChatScreenEvent.RemoveReaction -> {
                //removeReaction(event.messageId, event.reaction)
            }
        }
    }

    private fun sendMessage(text: String) {
        repository.sendMessage(
            streamName = state.value.stream, topicName = state.value.topic, text = text
        ).onEach(::processCompletableResource).launchIn(viewModelScope)
    }

    private fun addReaction(messageId: Long, reaction: Emoji) {
        repository.addReaction(messageId, reaction.name).onEach(::processCompletableResource)
            .launchIn(viewModelScope)
    }

    private fun removeReaction(messageId: Long, reaction: Emoji) {
        repository.removeReaction(messageId, reaction.name).onEach(::processCompletableResource)
            .launchIn(viewModelScope)
    }

    private fun processCompletableResource(resource: Resource<Unit>) {
        when (resource) {
            is Resource.Loading -> {
                _state.value = state.value.copy(
                    loading = true,
                )
            }

            is Resource.Success -> {
                _state.value = state.value.copy(
                    loading = false
                )
            }

            is Resource.Error -> {
                _state.value = state.value.copy(
                    loading = false
                )
                viewModelScope.launch {
                    _fragmentEventFlow.emit(ChatScreenFragmentEvent.ExplainError)
                }
            }
        }
    }
}