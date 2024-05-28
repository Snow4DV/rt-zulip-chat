package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_impl.util.PaginationConfig.PAGINATION_FETCH_MESSAGES_COUNT
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class LoadMoreMessagesUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    LoadMoreMessagesUseCase {
    override operator fun invoke(
        streamName: String,
        topicName: String,
        firstLoadedMessageId: Long?,
        includeAnchor: Boolean,
    ): Flow<Resource<ru.snowadv.chat_domain_api.model.ChatPaginatedMessages>> {
        return messagesRepository.getMessages(
            streamName = streamName,
            topicName = topicName,
            includeAnchorMessage = includeAnchor,
            anchorMessageId = firstLoadedMessageId,
            countOfMessages = PAGINATION_FETCH_MESSAGES_COUNT,
        )
    }
}