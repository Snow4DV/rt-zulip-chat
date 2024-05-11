package ru.snowadv.auth_data.di.holder

import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AuthDataAPI : BaseModuleAPI {
    val authRepo: AuthRepository
}