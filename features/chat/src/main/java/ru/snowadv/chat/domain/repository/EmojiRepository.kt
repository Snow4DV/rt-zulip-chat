package ru.snowadv.chat.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.model.ChatEmoji
import ru.snowadv.model.Resource

interface EmojiRepository {
    fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>>
}