package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.model.Resource

internal class ListenToMessagesUseCase(private val messagesRepository: MessageRepository) {
    operator fun invoke(
        streamName: String,
        topicName: String,
    ): Flow<Resource<List<ChatMessage>>> {
        return messagesRepository.listenToNewMessages(streamName, topicName)
    }
}