package ru.snowadv.chat.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.model.Resource

interface MessageRepository {
    /**
     * Returns flow that emits initial state of dialog ONCE.
     */
    fun getMessages(
        streamName: String,
        topicName: String,
    ): Flow<Resource<List<ChatMessage>>>

    /**
     * This flow should emit new message objects when they have changed.
     * Should emit Resource.Error before death.
     */
    fun listenToNewMessages(
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