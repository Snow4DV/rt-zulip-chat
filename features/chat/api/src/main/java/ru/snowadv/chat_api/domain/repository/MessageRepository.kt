package ru.snowadv.chat_api.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_api.domain.model.ChatMessage
import ru.snowadv.chat_api.domain.model.ChatPaginatedMessages
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
    ): Flow<Resource<ChatPaginatedMessages>>

    fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>>
    fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun removeReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
}