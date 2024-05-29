package ru.snowadv.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.snowadv.database.config.CacheConfiguration
import ru.snowadv.database.entity.MessageEntity
import ru.snowadv.database.entity.ReactionEntity

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages WHERE message_id = :id")
    suspend fun getMessage(id: Long): MessageEntity?

    @Query("SELECT * FROM messages WHERE message_id IN (:ids)")
    suspend fun getMessagesByIds(ids: List<Long>): List<MessageEntity>


    @Query("SELECT * FROM messages WHERE stream_name = :streamName AND subject = :topicName")
    suspend fun getMessagesFromTopic(streamName: String?, topicName: String): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE stream_name = :streamName")
    suspend fun getMessagesFromStream(streamName: String?): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE stream_id = :streamId AND subject = :topicName")
    suspend fun getMessagesFromTopic(streamId: Long?, topicName: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages  WHERE stream_name = :streamName AND subject = :topicName")
    suspend fun clearMessagesFromTopic(streamName: String?, topicName: String)

    @Query("DELETE FROM messages  WHERE stream_name = :streamName")
    suspend fun clearMessagesFromStream(streamName: String?)

    @Query("DELETE FROM messages  WHERE stream_id = :streamId AND subject = :topicName")
    suspend fun clearMessagesFromTopic(streamId: Long?, topicName: String)

    @Transaction
    suspend fun updateMessagesForTopicIfChanged(
        streamName: String?,
        topicName: String,
        messages: List<MessageEntity>
    ) {
        if (getMessagesFromTopic(streamName, topicName) != messages) {
            clearMessagesFromTopic(streamName, topicName)
            insertMessages(messages.takeLast(CacheConfiguration.CACHED_MESSAGES_COUNT_PER_TOPIC))
        }
    }

    @Transaction
    suspend fun updateMessagesForStreamIfChanged(
        streamName: String?,
        messages: List<MessageEntity>
    ) {
        if (getMessagesFromStream(streamName) != messages) {
            clearMessagesFromStream(streamName)
            val messagesLimitedByTopic = messages.groupBy { it.subject }
                .mapValues { it.value.takeLast(CacheConfiguration.CACHED_MESSAGES_COUNT_PER_TOPIC) }
                .entries.flatMap { it.value }
            insertMessages(messagesLimitedByTopic)
        }
    }

    @Transaction
    suspend fun addReactionToMessage(
        messageId: Long,
        emojiName: String,
        emojiCode: String,
        userId: Long
    ) {
        getMessage(messageId)?.let { message ->
            insertMessage(
                message.copy(
                    reactions = message.reactions + ReactionEntity(
                        userId = userId,
                        emojiName = emojiName,
                        emojiCode = emojiCode,
                    )
                )
            )
        }
    }

    @Transaction
    suspend fun removeReactionFromMessage(messageId: Long, emojiCode: String, userId: Long) {
        getMessage(messageId)?.let { message ->
            insertMessage(message.copy(
                reactions = message.reactions.filter { it.emojiCode != emojiCode || it.userId != userId }
            ))
        }
    }

    @Transaction
    suspend fun addFlagToMessages(messagesIds: List<Long>, flag: String) {
        getMessagesByIds(messagesIds).forEach { message ->
            insertMessage(
                message.copy(
                    flags = (message.flags + flag).distinct(),
                )
            )
        }
    }

    @Transaction
    suspend fun removeFlagFromMessages(messagesIds: List<Long>, flag: String) {
        getMessagesByIds(messagesIds).forEach { message ->
            insertMessage(
                message.copy(
                    flags = message.flags - flag
                )
            )
        }
    }

    @Transaction
    suspend fun updateMessageContent(messageId: Long, newContent: String) {
        getMessage(messageId)?.let { message ->
            insertMessage(
                message.copy(content = newContent),
            )
        }
    }

    @Query("DELETE FROM messages WHERE message_id = :messageId")
    suspend fun deleteMessage(messageId: Long)

    @Query("DELETE FROM messages")
    suspend fun clear()
}