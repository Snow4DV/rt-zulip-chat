package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface MoveMessageToOtherTopicUseCase {
    operator fun invoke(messageId: Long, newTopic: String): Flow<Resource<Unit>>
}