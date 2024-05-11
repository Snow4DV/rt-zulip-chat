package ru.snowadv.users_data_api.di

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.ZulipApi

interface UsersDataModuleDependencies : BaseModuleDependencies {
    val dispatcherProvider: DispatcherProvider
    val api: ZulipApi
    val authProvider: AuthProvider

    val usersDao: UsersDao
}
