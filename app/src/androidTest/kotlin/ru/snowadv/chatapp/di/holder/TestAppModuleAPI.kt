package ru.snowadv.chatapp.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.LoggerToggle

interface TestAppModuleAPI : BaseModuleAPI {
    val baseUrlProvider: BaseUrlProvider
    val authProviderMock: AuthProvider
    val authStorageMock: AuthStorageRepository
    val loggerToggle: LoggerToggle

    val emojisDao: EmojisDao
    val messagesDao: MessagesDao
    val streamsDao: StreamsDao
    val topicsDao: TopicsDao
    val usersDao: UsersDao
    val authDao: AuthDao
}