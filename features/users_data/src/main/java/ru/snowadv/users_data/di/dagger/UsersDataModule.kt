package ru.snowadv.users_data.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.users_data.repository.UserRepositoryImpl
import ru.snowadv.users_domain_api.repository.UserRepository

@Module
internal interface UsersDataModule {
    @Binds
    fun bindsUserRepositoryImpl(impl: UserRepositoryImpl): UserRepository
}