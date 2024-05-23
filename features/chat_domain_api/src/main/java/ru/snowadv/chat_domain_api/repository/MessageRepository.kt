package ru.snowadv.chat_domain_api.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource

interface MessageRepository {
    /**
     * Returns flow that emits initial state of dialog ONCE.
     */
    fun getMessagesFromTopic(
        streamName: String,
        topicName: String,
        includeAnchorMessage: Boolean,
        anchorMessageId: Long? = null,
        countOfMessages: Int,
        useCache: Boolean = false,
    ): Flow<Resource<ChatPaginatedMessages>>


    fun getMessagesFromStream(
        streamName: String,
        includeAnchorMessage: Boolean,
        anchorMessageId: Long? = null,
        countOfMessages: Int,
        useCache: Boolean = false,
    ): Flow<Resource<ChatPaginatedMessages>>

    fun getMessageByIdFromStream(
        messageId: Long,
        streamName: String,
    ): Flow<Resource<ChatMessage>>

    fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>>
    fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun removeReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun sendFile(streamName: String, topicName: String, inputStreamOpener: InputStreamOpener, mimeType: String?, extension: String?): Flow<Resource<Unit>>
}