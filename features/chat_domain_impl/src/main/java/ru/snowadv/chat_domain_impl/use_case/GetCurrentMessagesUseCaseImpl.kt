package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_impl.util.PaginationConfig.INIT_FETCH_MESSAGES_COUNT
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class GetCurrentMessagesUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    GetCurrentMessagesUseCase {
    override operator fun invoke(
        streamName: String,
        topicName: String?
    ): Flow<Resource<ChatPaginatedMessages>> {
        return if (topicName != null) {
            messagesRepository.getMessagesFromTopic(
                streamName = streamName,
                topicName = topicName,
                includeAnchorMessage = true,
                countOfMessages = INIT_FETCH_MESSAGES_COUNT,
                useCache = true,
            )
        } else {
            messagesRepository.getMessagesFromStream(
                streamName = streamName,
                includeAnchorMessage = true,
                countOfMessages = INIT_FETCH_MESSAGES_COUNT,
                useCache = true,
            )
        }
    }
}