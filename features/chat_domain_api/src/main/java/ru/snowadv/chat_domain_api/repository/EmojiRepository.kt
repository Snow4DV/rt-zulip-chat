package ru.snowadv.chat_domain_api.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.model.Resource

interface EmojiRepository {
    fun getAvailableEmojis(): Flow<Resource<List<ChatEmoji>>>
}