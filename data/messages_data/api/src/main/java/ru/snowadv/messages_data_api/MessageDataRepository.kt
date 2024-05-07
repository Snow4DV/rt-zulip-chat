package ru.snowadv.messages_data_api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.messages_data_api.model.DataPaginatedMessages
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.model.Resource
import java.io.File
import java.io.InputStream

interface MessageDataRepository {
    /**
     * Method returns messages from current topic _once_ (!)
     *
     * @param anchorMessageId If it is not passed, query will be anchored by the newest message
     */
    fun getMessages(
        streamName: String,
        topicName: String,
        includeAnchorMessage: Boolean,
        countOfMessages: Int,
        anchorMessageId: Long? = null,
        showAndUpdateCache: Boolean = false,
    ): Flow<Resource<DataPaginatedMessages>>

    fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>>

    fun sendFile(
        streamName: String,
        topicName: String,
        mimeType: String?,
        inputStreamOpener: InputStreamOpener,
        extension: String?,
    ): Flow<Resource<Unit>>

    fun addReactionToMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun removeReactionFromMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>>
}