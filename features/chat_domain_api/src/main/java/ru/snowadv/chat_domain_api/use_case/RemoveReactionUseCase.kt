package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface RemoveReactionUseCase {
    operator fun invoke(messageId: Long, reactionName: String): Flow<Resource<Unit>>
}