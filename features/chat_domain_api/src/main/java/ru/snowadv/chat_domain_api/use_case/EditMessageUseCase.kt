package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface EditMessageUseCase {
    operator fun invoke(messageId: Long, newContent: String): Flow<Resource<Unit>>
}