package ru.snowadv.voiceapp.di.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.snowadv.voiceapp.ChatApp
import ru.snowadv.voiceapp.di.holder.AppModuleAPI
import ru.snowadv.voiceapp.di.holder.AppModuleDependencies
import javax.inject.Singleton

@Component(
    dependencies = [AppModuleDependencies::class],
    modules = [AppNavigationModule::class, AppRepositoryModule::class],
)
@Singleton
internal interface AppComponent : AppModuleAPI {
    fun inject(chatApp: ChatApp)

    @Component.Factory
    interface Factory {
        fun create(deps: AppModuleDependencies): AppComponent
    }

    companion object {
        fun initAndGet(deps: AppModuleDependencies): AppComponent {
            return DaggerAppComponent.factory().create(deps)
        }
    }
}