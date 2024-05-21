package ru.snowadv.auth_api.di

import ru.snowadv.auth_api.domain.navigation.AuthRouter
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AuthFeatureDependencies : BaseModuleDependencies {
    val router: AuthRouter
    val authDataRepo: AuthDataRepository
}