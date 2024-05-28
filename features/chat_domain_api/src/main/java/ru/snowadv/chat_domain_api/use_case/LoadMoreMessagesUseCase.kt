package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.model.Resource

interface LoadMoreMessagesUseCase {
    operator fun invoke(
        streamName: String,
        topicName: String,
        firstLoadedMessageId: Long?,
        includeAnchor: Boolean,
    ): Flow<Resource<ChatPaginatedMessages>>
}