package ru.snowadv.auth_data_impl.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.auth_data_impl.AuthDataRepositoryImpl
import ru.snowadv.auth_data_impl.AuthProviderImpl

@Module
internal interface AuthDataModule {
    @Binds
    fun bindAuthDataRepositoryImpl(authDataRepositoryImpl: AuthDataRepositoryImpl): AuthDataRepository
    @Binds
    fun bindAuthProviderImpl(authProviderImpl: AuthProviderImpl): AuthProvider
}