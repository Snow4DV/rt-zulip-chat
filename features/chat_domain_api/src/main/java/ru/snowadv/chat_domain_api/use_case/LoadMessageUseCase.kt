package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.model.Resource

interface LoadMessageUseCase {
    operator fun invoke(
        messageId: Long,
        streamName: String,
    ): Flow<Resource<ChatMessage>>
}