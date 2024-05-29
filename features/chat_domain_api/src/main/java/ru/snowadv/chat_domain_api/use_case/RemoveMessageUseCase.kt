package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface RemoveMessageUseCase {
    operator fun invoke(messageId: Long): Flow<Resource<Unit>>
}