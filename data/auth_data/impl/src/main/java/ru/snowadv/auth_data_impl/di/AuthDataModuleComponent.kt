package ru.snowadv.auth_data_impl.di

import dagger.Component
import ru.snowadv.auth_data_api.di.AuthDataModuleAPI
import ru.snowadv.auth_data_api.di.AuthDataModuleDependencies
import ru.snowadv.module_injector.general.PerFeature
import javax.inject.Singleton

@Singleton
@Component(dependencies = [AuthDataModuleDependencies::class], modules = [AuthDataModule::class])
internal interface AuthDataModuleComponent : AuthDataModuleAPI {
    companion object {
        fun initAndGet(deps: AuthDataModuleDependencies): AuthDataModuleComponent {
            return DaggerAuthDataModuleComponent.builder()
                .authDataModuleDependencies(deps)
                .build()
        }
    }
}