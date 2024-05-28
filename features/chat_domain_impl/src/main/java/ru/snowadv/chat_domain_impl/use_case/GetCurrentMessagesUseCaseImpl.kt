package ru.snowadv.chat_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_impl.util.PaginationConfig.INIT_FETCH_MESSAGES_COUNT
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class GetCurrentMessagesUseCaseImpl @Inject constructor(private val messagesRepository: MessageRepository) :
    ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase {
    override operator fun invoke(
        streamName: String,
        topicName: String,
    ): Flow<Resource<ru.snowadv.chat_domain_api.model.ChatPaginatedMessages>> {
        return messagesRepository.getMessages(
            streamName = streamName,
            topicName = topicName,
            includeAnchorMessage = true,
            countOfMessages = INIT_FETCH_MESSAGES_COUNT,
            useCache = true,
        )
    }
}