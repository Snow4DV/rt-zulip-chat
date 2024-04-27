package ru.snowadv.voiceapp

import android.app.Application
import ru.snowadv.navigation.application.NavigationHolder
import ru.snowadv.navigation.application.impl.AppNavigationHolder
import ru.snowadv.voiceapp.di.legacy.MainGraph
import ru.snowadv.voiceapp.glue.injector.ModulesInjector
import ru.snowadv.voiceapp.navigation.Screens

internal class ChatApp: Application(), NavigationHolder by AppNavigationHolder() {
    override fun onCreate() {
        ModulesInjector.inject(this)
        super.onCreate()
        MainGraph.init(router)
        router.newRootScreen(Screens.Home())
    }
}