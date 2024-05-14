package ru.snowadv.properties_provider_impl.di

import dagger.Component
import ru.snowadv.properties_provider_api.di.PropertiesProviderModuleAPI
import ru.snowadv.properties_provider_api.di.PropertiesProviderModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [PropertiesProviderModuleDependencies::class], modules = [PropertiesProviderModule::class])
internal interface PropertiesProviderModuleComponent : PropertiesProviderModuleAPI {
    companion object {
        fun initAndGet(deps: PropertiesProviderModuleDependencies): PropertiesProviderModuleComponent {
            return DaggerPropertiesProviderModuleComponent.builder()
                .propertiesProviderModuleDependencies(deps)
                .build()
        }
    }
}