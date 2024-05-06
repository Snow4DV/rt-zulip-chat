package ru.snowadv.auth_data_api.di

import ru.snowadv.database.dao.AuthDao
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network_authorizer.api.ZulipAuthApi

interface AuthDataModuleDependencies : BaseModuleDependencies {
    val authApi: ZulipAuthApi
    val authDao: AuthDao
}