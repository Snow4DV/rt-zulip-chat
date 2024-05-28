package ru.snowadv.chatapp.di.dagger

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.chatapp.glue.auth.BadAuthBehaviorImpl
import ru.snowadv.chatapp.glue.coroutines.DispatcherProviderImpl
import ru.snowadv.chatapp.glue.network.ReleaseLoggerToggle
import ru.snowadv.network.api.LoggerToggle

@Module
internal interface AppModule {

    @Binds
    fun bindDispatcherProviderImpl(dispatcherProviderImpl: DispatcherProviderImpl): DispatcherProvider
    @Binds
    fun bindBadAuthBehaviorImpl(badAuthBehaviorImpl: BadAuthBehaviorImpl): BadAuthBehavior
    @Binds
    fun bindLoggerToggleImpl(impl: ReleaseLoggerToggle): LoggerToggle

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