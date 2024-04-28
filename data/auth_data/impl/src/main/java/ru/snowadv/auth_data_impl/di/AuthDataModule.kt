package ru.snowadv.auth_data_impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_impl.AuthDataRepositoryImpl

@Module
internal interface AuthDataModule {
    @Binds
    fun bindAuthDataRepositoryImpl(authDataRepositoryImpl: AuthDataRepositoryImpl): AuthDataRepository
}