package ru.snowadv.voiceapp.di.dagger

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.voiceapp.glue.auth.BadAuthBehaviorImpl
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl

@Module
internal interface AppModule {

    @Binds
    fun bindDispatcherProviderImpl(dispatcherProviderImpl: DispatcherProviderImpl): DispatcherProvider
    @Binds
    fun bindBadAuthBehaviorImpl(badAuthBehaviorImpl: BadAuthBehaviorImpl): BadAuthBehavior

    companion object {
        @Reusable
        @Provides
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
            }
        }
    }
}