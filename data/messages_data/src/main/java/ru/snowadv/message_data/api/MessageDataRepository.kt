package ru.snowadv.message_data.api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.message_data.model.DataMessage
import ru.snowadv.model.Resource

interface MessageDataRepository {
    /**
     * Method returns messages from current topic _once_ (!)
     */
    fun getMessages(
        streamName: String,
        topicName: String,
    ): Flow<Resource<List<DataMessage>>>
    fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>>
    fun addReactionToMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>>
    fun removeReactionFromMessage(messageId: Long, reactionName: String): Flow<Resource<Unit>>
}