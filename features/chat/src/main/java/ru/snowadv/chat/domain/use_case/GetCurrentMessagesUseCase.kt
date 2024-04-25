package ru.snowadv.chat.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatPaginatedMessages
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.chat.domain.util.PaginationConfig
import ru.snowadv.model.Resource

internal class GetCurrentMessagesUseCase(private val messagesRepository: MessageRepository) {
    operator fun invoke(
        streamName: String,
        topicName: String,
    ): Flow<Resource<ChatPaginatedMessages>> {
        return messagesRepository.getMessages(
            streamName = streamName,
            topicName = topicName,
            includeAnchorMessage = true,
            countOfMessages = PaginationConfig.MESSAGES_COUNT_PER_FETCH,
        )
    }
}