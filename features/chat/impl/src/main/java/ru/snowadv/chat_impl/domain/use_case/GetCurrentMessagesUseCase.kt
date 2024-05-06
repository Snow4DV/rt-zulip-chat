package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_impl.domain.model.ChatPaginatedMessages
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.chat_impl.domain.util.PaginationConfig.MESSAGES_COUNT_PER_FETCH
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class GetCurrentMessagesUseCase @Inject constructor(private val messagesRepository: MessageRepository) {
    operator fun invoke(
        streamName: String,
        topicName: String,
    ): Flow<Resource<ChatPaginatedMessages>> {
        return messagesRepository.getMessages(
            streamName = streamName,
            topicName = topicName,
            includeAnchorMessage = true,
            countOfMessages = MESSAGES_COUNT_PER_FETCH,
            saveToCache = true,
        )
    }
}