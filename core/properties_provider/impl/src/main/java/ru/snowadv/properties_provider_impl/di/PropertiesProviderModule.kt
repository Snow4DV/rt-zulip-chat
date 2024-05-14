package ru.snowadv.properties_provider_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.properties_provider_api.AuthUserPropertyRepository
import ru.snowadv.properties_provider_impl.AuthUserPropertyRepositoryImpl

@Module
internal interface PropertiesProviderModule {
    @Binds
    fun binAuthUserPropertyRepoImpl(authUserPropertyRepositoryImpl: AuthUserPropertyRepositoryImpl): AuthUserPropertyRepository
}