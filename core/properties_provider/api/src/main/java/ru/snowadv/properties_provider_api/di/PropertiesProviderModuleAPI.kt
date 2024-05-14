package ru.snowadv.properties_provider_api.di

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.properties_provider_api.AuthUserPropertyRepository

interface PropertiesProviderModuleAPI : BaseModuleAPI {
    val authUserPropertyRepository: AuthUserPropertyRepository
}