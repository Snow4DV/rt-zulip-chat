package ru.snowadv.chatapp.mock.dao

import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.entity.AuthUserEntity
import ru.snowadv.database.entity.EmojiEntity
import ru.snowadv.database.entity.MessageEntity
import ru.snowadv.database.entity.StreamEntity
import ru.snowadv.database.entity.TopicEntity
import ru.snowadv.database.entity.UserEntity
import javax.inject.Inject

internal class AuthDaoMockImpl @Inject constructor(): AuthDao {
    override suspend fun getCurrentAuth(): AuthUserEntity? {
        return null
    }

    override suspend fun clearAuth() {}

    override suspend fun insert(authUserEntity: AuthUserEntity) {}
}

internal class EmojisDaoMockImpl @Inject constructor(): EmojisDao {
    override suspend fun getEmojis(): List<EmojiEntity> {
        return emptyList()
    }

    override suspend fun insertEmojis(emojis: List<EmojiEntity>) {}

    override suspend fun clear() {}
}
internal class MessagesDaoMockImpl @Inject constructor(): MessagesDao {
    override suspend fun getMessage(id: Long): MessageEntity? {
        return null
    }

    override suspend fun getMessagesByIds(ids: List<Long>): List<MessageEntity> {
        return emptyList()
    }

    override suspend fun getMessagesFromTopic(
        streamName: String?,
        topicName: String
    ): List<MessageEntity> {
        return emptyList()
    }

    override suspend fun getMessagesFromTopic(
        streamId: Long?,
        topicName: String
    ): List<MessageEntity> {
        return emptyList()
    }

    override suspend fun insertMessages(messages: List<MessageEntity>) {}

    override suspend fun insertMessage(message: MessageEntity) {}

    override suspend fun clearMessagesFromTopic(streamName: String?, topicName: String) {}

    override suspend fun clearMessagesFromTopic(streamId: Long?, topicName: String) {}

    override suspend fun deleteMessage(messageId: Long) {}

    override suspend fun clear() {}

}

internal class StreamsDaoMockImpl @Inject constructor(): StreamsDao {
    override suspend fun getAll(): List<StreamEntity> {
        return emptyList()
    }

    override suspend fun getSubscribed(): List<StreamEntity> {
        return emptyList()
    }

    override suspend fun insert(streamEntity: StreamEntity) {}

    override suspend fun insertAll(streams: List<StreamEntity>) {}

    override suspend fun clear() {}

    override suspend fun removeStreamById(streamId: Long) {}

}

internal class TopicsDaoMockImpl @Inject constructor(): TopicsDao {
    override suspend fun getTopicsByStreamId(streamId: Long): List<TopicEntity> {
        return emptyList()
    }

    override suspend fun insert(topicEntity: TopicEntity) {}

    override suspend fun insertAll(topics: List<TopicEntity>) {}

    override suspend fun clearByStreamId(streamId: Long) {}

    override suspend fun clear() {}

    override suspend fun clearTopicsForStreamId(streamId: Long) {}
}

internal class UsersDaoMockImpl @Inject constructor(): UsersDao {
    override suspend fun getAllUsers(): List<UserEntity> {
        return emptyList()
    }

    override suspend fun getUser(userId: Long): UserEntity? {
        return null
    }

    override suspend fun clear() {}

    override suspend fun insert(userEntity: UserEntity) {}

    override suspend fun insertAll(users: List<UserEntity>) {}

}