package ru.snowadv.chat_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.model.Resource

interface GetEmojisUseCase {
    operator fun invoke(): Flow<Resource<List<ChatEmoji>>>
}