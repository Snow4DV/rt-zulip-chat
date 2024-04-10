package ru.snowadv.navigation.application.impl

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.snowadv.navigation.application.NavigationHolder

class AppNavigationHolder: NavigationHolder {
    override val cicerone: Cicerone<Router> = Cicerone.create()
}