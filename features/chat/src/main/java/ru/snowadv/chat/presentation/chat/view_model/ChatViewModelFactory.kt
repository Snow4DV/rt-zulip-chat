package ru.snowadv.chat.presentation.chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.use_case.AddReactionUseCase
import ru.snowadv.chat.domain.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat.domain.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat.domain.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat.domain.use_case.SendMessageUseCase

internal class ChatViewModelFactory(
    private val router: ChatRouter,
    private val streamName: String,
    private val topicName: String,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val getCurrentMessagesUseCase: GetCurrentMessagesUseCase,
    private val listenToChatEventsUseCase: ListenToChatEventsUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val loadMoreMessagesUseCase: LoadMoreMessagesUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(
                router = router,
                streamName = streamName,
                topicName = topicName,
                addReactionUseCase = addReactionUseCase,
                removeReactionUseCase = removeReactionUseCase,
                sendMessageUseCase = sendMessageUseCase,
                getMessagesUseCase = getCurrentMessagesUseCase,
                listenToChatEventsUseCase = listenToChatEventsUseCase,
                loadMoreMessagesUseCase = loadMoreMessagesUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}