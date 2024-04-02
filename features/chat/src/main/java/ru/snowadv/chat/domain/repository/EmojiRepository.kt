package ru.snowadv.chat.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.domain.model.Resource

internal interface EmojiRepository {
    suspend fun getAvailableEmojis(): Flow<Resource<List<Emoji>>>
}