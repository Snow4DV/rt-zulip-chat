package ru.snowadv.chat.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.domain.model.Resource

internal interface MessageRepository {
    /**
     * Returns flow that emits new state of dialog each time it updates. Shoul
     */
    fun getMessages(
        streamName: String,
        topicName: String,
    ): Flow<Resource<List<ChatMessage>>>
    fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>>
    fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun removeReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
}