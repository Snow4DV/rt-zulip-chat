package ru.snowadv.auth_api.di

import ru.snowadv.auth_api.presentation.AuthScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface AuthFeatureAPI : BaseModuleAPI {
    val screenFactory: AuthScreenFactory
}