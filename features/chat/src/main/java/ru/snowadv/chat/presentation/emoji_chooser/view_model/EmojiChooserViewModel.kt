package ru.snowadv.chat.presentation.emoji_chooser.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.snowadv.chat.data.repository.StubEmojiRepository
import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.chat.domain.repository.EmojiRepository
import ru.snowadv.chat.presentation.emoji_chooser.event.EmojiChooserEvent
import ru.snowadv.chat.presentation.emoji_chooser.event.EmojiChooserFragmentEvent
import ru.snowadv.chat.presentation.emoji_chooser.state.ChooserScreenStatus
import ru.snowadv.chat.presentation.emoji_chooser.state.EmojiChooserState
import ru.snowadv.chat.presentation.util.toUiChatEmoji
import ru.snowadv.domain.model.Resource

internal class EmojiChooserViewModel(
    private val repo: EmojiRepository = StubEmojiRepository(), // TODO Replace with DI
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO, // TODO Replace with DI
): ViewModel() {


    private val _state = MutableStateFlow(EmojiChooserState())
    val state: StateFlow<EmojiChooserState> = _state

    private val _fragmentEventFlow = MutableSharedFlow<EmojiChooserFragmentEvent>()
    val fragmentEventFlow: SharedFlow<EmojiChooserFragmentEvent> = _fragmentEventFlow

    init {
        getEmojisfromRepository()
    }

    fun event(event: EmojiChooserEvent) {
        when(event) {
            is EmojiChooserEvent.OnEmojiChosen -> {
                viewModelScope.launch {
                    _fragmentEventFlow.emit(EmojiChooserFragmentEvent.CloseWithChosenEmoji(event.emoji))
                }
            }
        }
    }

    private fun getEmojisfromRepository() {
        viewModelScope.launch(ioDispatcher) {
            repo.getAvailableEmojis().onEach(::handleEmojiResource).launchIn(this)
        }
    }

    private fun handleEmojiResource(res: Resource<List<Emoji>>) {
        when(res) {
            is Resource.Error -> {
                _state.value = state.value.copy(
                    status = ChooserScreenStatus.ERROR
                )
            }
            is Resource.Loading -> {
                _state.value = state.value.copy(
                    status = ChooserScreenStatus.LOADING
                )
            }
            is Resource.Success -> {
                _state.value = state.value.copy(
                    status = ChooserScreenStatus.OK,
                    emojis = res.data.map { it.toUiChatEmoji() }
                )
            }
        }
    }
}