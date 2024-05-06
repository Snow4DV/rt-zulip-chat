package ru.snowadv.auth_data_api.di

import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.module_injector.module.BaseModuleAPI

interface AuthDataModuleAPI : BaseModuleAPI {
    val authDataRepo: AuthDataRepository
    val authProvider: AuthProvider
}