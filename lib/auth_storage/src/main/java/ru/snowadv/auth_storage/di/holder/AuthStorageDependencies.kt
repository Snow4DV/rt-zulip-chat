package ru.snowadv.auth_storage.di.holder

import ru.snowadv.database.dao.AuthDao
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network_authorizer.api.ZulipAuthApi

interface AuthStorageDependencies : BaseModuleDependencies {
    val zulipAuthApi: ZulipAuthApi
    val authDao: AuthDao
}