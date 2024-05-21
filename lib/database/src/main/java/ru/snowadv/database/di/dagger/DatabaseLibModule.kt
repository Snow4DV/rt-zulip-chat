package ru.snowadv.database.di.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.snowadv.database.converter.CacheDatabaseTypeConverter
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.db.ChatDatabase
import javax.inject.Singleton

@Module
internal class DatabaseLibModule {
    @Provides
    @Singleton
    fun provideDatabase(appContext: Context, converter: CacheDatabaseTypeConverter): ChatDatabase {
        return Room.databaseBuilder(appContext, ChatDatabase::class.java, ChatDatabase.DATABASE_NAME)
            .addTypeConverter(converter)
            .build()
    }

    @Provides
    @Singleton
    fun provideEmojisDao(db: ChatDatabase): EmojisDao {
        return db.emojisDao
    }

    @Provides
    @Singleton
    fun provideMessagesDao(db: ChatDatabase): MessagesDao {
        return db.messagesDao
    }

    @Provides
    @Singleton
    fun provideStreamsDao(db: ChatDatabase): StreamsDao {
        return db.streamsDao
    }


    @Provides
    @Singleton
    fun provideTopicsDao(db: ChatDatabase): TopicsDao {
        return db.topicsDao
    }

    @Provides
    @Singleton
    fun provideUsersDao(db: ChatDatabase): UsersDao {
        return db.usersDao
    }

    @Provides
    @Singleton
    fun provideAuthDao(db: ChatDatabase): AuthDao {
        return db.authDao
    }
}