package ru.snowadv.auth_storage.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface AuthStorageAPI : BaseModuleAPI {
    val authStorageRepository: AuthStorageRepository
    val authProvider: AuthProvider
}