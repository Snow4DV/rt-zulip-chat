package ru.snowadv.chat.presentation.chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.chat.presentation.navigation.ChatRouter

internal class ChatViewModelFactory(
    private val router: ChatRouter,
    private val streamId: Long,
    private val topicName: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(router = router, streamId = streamId, topicName = topicName) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}