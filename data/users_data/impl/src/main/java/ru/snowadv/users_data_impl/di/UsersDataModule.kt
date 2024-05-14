package ru.snowadv.users_data_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.users_data_api.UserDataRepository
import ru.snowadv.users_data_impl.UserDataRepositoryImpl

@Module
internal interface UsersDataModule {
    @Binds
    fun bindUserDataRepoImpl(userDataRepositoryImpl: UserDataRepositoryImpl): UserDataRepository
}