package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface ChangeMessageReadStateUseCase {
    operator fun invoke(
        messagesIds: List<Long>,
        newState: Boolean,
    ): Flow<Resource<Unit>>
}