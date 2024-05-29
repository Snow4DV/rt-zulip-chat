package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.model.Resource

interface GetCurrentMessagesUseCase {
    operator fun invoke(
        streamName: String,
        topicName: String?,
    ): Flow<Resource<ChatPaginatedMessages>>
}