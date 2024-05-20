package ru.snowadv.users_data.di.dagger

import dagger.Component
import ru.snowadv.users_data.di.holder.UsersDataAPI
import ru.snowadv.users_data.di.holder.UsersDataDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [UsersDataDependencies::class], modules = [UsersDataModule::class])
@PerScreen
internal interface UsersDataComponent : UsersDataAPI {
    companion object {
        fun initAndGet(deps: UsersDataDependencies): UsersDataComponent {
            return DaggerUsersDataComponent.builder()
                .usersDataDependencies(deps)
                .build()
        }
    }
}