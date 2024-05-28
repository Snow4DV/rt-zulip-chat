package ru.snowadv.auth_data.di.dagger

import dagger.Component
import ru.snowadv.auth_data.di.holder.AuthDataAPI
import ru.snowadv.auth_data.di.holder.AuthDataDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [AuthDataDependencies::class], modules = [AuthDataModule::class])
@PerScreen
internal interface AuthDataComponent : AuthDataAPI {
    companion object {
        fun initAndGet(deps: AuthDataDependencies): AuthDataComponent {
            return DaggerAuthDataComponent.builder()
                .authDataDependencies(deps)
                .build()
        }
    }
}