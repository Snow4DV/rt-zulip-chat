package ru.snowadv.users_data.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface UsersDataDependencies : BaseModuleDependencies {
    val zulipApi: ZulipApi
    val usersDao: UsersDao
    val dispatcherProvider: DispatcherProvider
    val authProvider: AuthProvider
}