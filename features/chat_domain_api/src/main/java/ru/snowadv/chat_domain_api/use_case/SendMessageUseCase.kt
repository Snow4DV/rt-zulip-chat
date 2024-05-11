package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface SendMessageUseCase {
    operator fun invoke(
        streamName: String,
        topicName: String,
        text: String,
    ): Flow<Resource<Unit>>
}