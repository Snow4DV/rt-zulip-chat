package ru.snowadv.chat.presentation.chat.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.chat.domain.use_case.AddReactionUseCase
import ru.snowadv.chat.domain.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat.domain.use_case.ListenToMessagesUseCase
import ru.snowadv.chat.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat.domain.use_case.SendMessageUseCase

internal class ChatViewModelFactory(
    private val router: ChatRouter,
    private val streamName: String,
    private val topicName: String,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase,
    private val getCurrentMessagesUseCase: GetCurrentMessagesUseCase,
    private val listenToMessagesUseCase: ListenToMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
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
                listenToMessagesUseCase = listenToMessagesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}