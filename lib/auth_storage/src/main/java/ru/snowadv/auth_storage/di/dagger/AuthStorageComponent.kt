package ru.snowadv.auth_storage.di.dagger

import dagger.Component
import ru.snowadv.auth_storage.di.holder.AuthStorageAPI
import ru.snowadv.auth_storage.di.holder.AuthStorageDependencies
import ru.snowadv.module_injector.general.PerScreen
import javax.inject.Singleton

@Component(dependencies = [AuthStorageDependencies::class], modules = [AuthStorageModule::class])
@Singleton
internal interface AuthStorageComponent : AuthStorageAPI {
    companion object {
        fun initAndGet(deps: AuthStorageDependencies): AuthStorageComponent {
            return DaggerAuthStorageComponent.builder()
                .authStorageDependencies(deps)
                .build()
        }
    }
}