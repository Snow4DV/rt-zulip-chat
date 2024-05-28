package ru.snowadv.auth_data.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.auth_data.repository.AuthRepositoryImpl
import ru.snowadv.auth_domain_api.repository.AuthRepository

@Module
internal interface AuthDataModule {
    @Binds
    fun bindsAuthRepoImpl(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}