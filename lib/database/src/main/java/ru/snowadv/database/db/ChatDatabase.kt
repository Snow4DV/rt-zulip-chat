package ru.snowadv.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.snowadv.database.converter.CacheDatabaseTypeConverter
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.entity.AuthUserEntity
import ru.snowadv.database.entity.EmojiEntity
import ru.snowadv.database.entity.MessageEntity
import ru.snowadv.database.entity.ReactionEntity
import ru.snowadv.database.entity.StreamEntity
import ru.snowadv.database.entity.TopicEntity
import ru.snowadv.database.entity.UserEntity

@Database(
    entities = [
        EmojiEntity::class,
        MessageEntity::class,
        StreamEntity::class,
        TopicEntity::class,
        UserEntity::class,
        AuthUserEntity::class,
    ],
    version = 1,
)
@TypeConverters(CacheDatabaseTypeConverter::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract val emojisDao: EmojisDao
    abstract val messagesDao: MessagesDao
    abstract val streamsDao: StreamsDao
    abstract val topicsDao: TopicsDao
    abstract val usersDao: UsersDao
    abstract val authDao: AuthDao

    companion object {
        const val DATABASE_NAME = "chat_database"
    }
}