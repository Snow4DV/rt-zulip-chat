package ru.snowadv.voiceapp

import android.app.Application
import ru.snowadv.navigation.application.NavigationHolder
import ru.snowadv.navigation.application.impl.AppNavigationHolder
import ru.snowadv.voiceapp.di.Graph
import ru.snowadv.voiceapp.navigation.Screens

internal class ChatApp: Application(), NavigationHolder by AppNavigationHolder() {
    override fun onCreate() {
        super.onCreate()
        Graph.init(router)
        router.navigateTo(Screens.Home())
    }
}