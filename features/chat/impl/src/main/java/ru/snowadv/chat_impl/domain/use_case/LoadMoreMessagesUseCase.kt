package ru.snowadv.chat_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_api.domain.model.ChatPaginatedMessages
import ru.snowadv.chat_api.domain.repository.MessageRepository
import ru.snowadv.chat_impl.domain.util.PaginationConfig.MESSAGES_COUNT_PER_FETCH
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class LoadMoreMessagesUseCase @Inject constructor(private val messagesRepository: MessageRepository) {
    operator fun invoke(
        streamName: String,
        topicName: String,
        firstLoadedMessageId: Long?,
        includeAnchor: Boolean,
    ): Flow<Resource<ChatPaginatedMessages>> {
        return messagesRepository.getMessages(
            streamName = streamName,
            topicName = topicName,
            includeAnchorMessage = includeAnchor,
            anchorMessageId = firstLoadedMessageId,
            countOfMessages = MESSAGES_COUNT_PER_FETCH,
        )
    }
}