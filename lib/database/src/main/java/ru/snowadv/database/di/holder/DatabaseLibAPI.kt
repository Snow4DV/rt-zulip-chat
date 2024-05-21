package ru.snowadv.database.di.holder

import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.db.ChatDatabase
import ru.snowadv.module_injector.module.BaseModuleAPI

interface DatabaseLibAPI : BaseModuleAPI {
    val database: ChatDatabase

    val emojisDao: EmojisDao
    val messagesDao: MessagesDao
    val streamsDao: StreamsDao
    val topicsDao: TopicsDao
    val usersDao: UsersDao
    val authDao: AuthDao
}