package ru.snowadv.auth_data_api.di

import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.properties_provider_api.AuthUserPropertyRepository

interface AuthDataModuleDependencies : BaseModuleDependencies {
    val authProviderPropertyRepository: AuthUserPropertyRepository
}