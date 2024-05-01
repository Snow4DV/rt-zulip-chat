package ru.snowadv.voiceapp

import android.app.Application
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.snowadv.navigation.application.NavigationHolder
import ru.snowadv.navigation.application.impl.AppNavigationHolder
import ru.snowadv.voiceapp.di.dagger.AppComponent
import ru.snowadv.voiceapp.di.holder.AppModuleComponentHolder
import ru.snowadv.voiceapp.glue.injector.ModulesInjector
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

internal class ChatApp: Application() {
    private lateinit var appComponent: AppComponent // Store ref to app component so it is not destroyed during configuration changes
    override fun onCreate() {
        ModulesInjector.inject(this)
        appComponent = AppModuleComponentHolder.getComponent()
        super.onCreate()
    }
}