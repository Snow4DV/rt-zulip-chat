package ru.snowadv.chat_impl.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_impl.domain.model.ChatMessage
import ru.snowadv.chat_impl.domain.model.ChatPaginatedMessages
import ru.snowadv.model.Resource

interface MessageRepository {
    /**
     * Returns flow that emits initial state of dialog ONCE.
     */
    fun getMessages(
        streamName: String,
        topicName: String,
        includeAnchorMessage: Boolean,
        anchorMessageId: Long? = null,
        countOfMessages: Int,
        saveToCache: Boolean = false,
    ): Flow<Resource<ChatPaginatedMessages>>

    fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>>
    fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun removeReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
}