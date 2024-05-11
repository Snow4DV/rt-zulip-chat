package ru.snowadv.auth_storage.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.auth_storage.provider.AuthProviderImpl
import ru.snowadv.auth_storage.repository.AuthStorageRepositoryImpl
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository

@Module
internal interface AuthStorageModule {
    @Binds
    fun bindsAuthStorageRepoImpl(impl: AuthStorageRepositoryImpl): AuthStorageRepository
    @Binds
    fun bindsAuthProviderImpl(authProviderImpl: AuthProviderImpl): AuthProvider
}