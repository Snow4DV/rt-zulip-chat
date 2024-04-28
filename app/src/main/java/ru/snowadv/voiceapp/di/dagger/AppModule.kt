package ru.snowadv.voiceapp.di.dagger

import dagger.Binds
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.voiceapp.glue.auth.AuthProviderImpl
import ru.snowadv.voiceapp.glue.auth.BadAuthBehaviorImpl
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl

interface AppModule {

    @Binds
    fun bindAuthProviderImpl(authProviderImpl: AuthProviderImpl): AuthProvider
    @Binds
    fun bindDispatcherProviderImpl(dispatcherProviderImpl: DispatcherProviderImpl): DispatcherProvider
    @Binds
    fun bindBadAuthBehaviorImpl(badAuthBehaviorImpl: BadAuthBehaviorImpl): BadAuthBehavior

    companion object {
        @Provides
        @Reusable
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
            }
        }
    }
}