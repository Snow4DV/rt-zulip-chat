package ru.snowadv.users_data_impl.di

import dagger.Component
import ru.snowadv.users_data_api.di.UsersDataModuleAPI
import ru.snowadv.users_data_api.di.UsersDataModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [UsersDataModuleDependencies::class], modules = [UsersDataModule::class])
internal interface UsersDataModuleComponent : UsersDataModuleAPI {
    companion object {
        fun initAndGet(deps: UsersDataModuleDependencies): UsersDataModuleComponent {
            return DaggerUsersDataModuleComponent.builder()
                .usersDataModuleDependencies(deps)
                .build()
        }
    }
}