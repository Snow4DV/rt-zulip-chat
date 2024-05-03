package ru.snowadv.chat_impl.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_impl.domain.model.ChatEmoji
import ru.snowadv.model.Resource

interface EmojiRepository {
    fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>>
}