package ru.snowadv.chatapp.di.dagger

import dagger.Component
import ru.snowadv.chatapp.ChatApp
import ru.snowadv.chatapp.activity.MainActivity
import ru.snowadv.chatapp.di.holder.AppModuleAPI
import ru.snowadv.chatapp.di.holder.AppModuleDependencies
import javax.inject.Singleton

@Component(
    dependencies = [AppModuleDependencies::class],
    modules = [AppNavigationModule::class, AppModule::class],
)
@Singleton
internal interface AppComponent : AppModuleAPI {
    fun inject(mainActivity: MainActivity)

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