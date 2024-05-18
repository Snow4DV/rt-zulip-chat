package ru.snowadv.chatapp.di.holder

import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.LoggerToggle

interface TestAppModuleAPI : BaseModuleAPI {
    val baseUrlProvider: BaseUrlProvider
    val authProviderMock: AuthProvider
    val authStorageMock: AuthStorageRepository
    val loggerToggle: LoggerToggle
}