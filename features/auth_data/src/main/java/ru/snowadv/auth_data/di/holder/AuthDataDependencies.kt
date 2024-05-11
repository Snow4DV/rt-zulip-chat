package ru.snowadv.auth_data.di.holder

import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AuthDataDependencies : BaseModuleDependencies {
    val authStorageRepository: AuthStorageRepository
}