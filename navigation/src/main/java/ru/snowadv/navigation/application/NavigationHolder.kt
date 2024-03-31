package ru.snowadv.navigation.application

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

interface NavigationHolder {
    val cicerone: Cicerone<Router>

    val router get() = cicerone.router
    val navigationHolder get() = cicerone.getNavigatorHolder()
}