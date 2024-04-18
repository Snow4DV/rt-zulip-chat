package ru.snowadv.voiceapp.di.deps

import ru.snowadv.home.di.HomeDeps
import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.voiceapp.glue.navigation.HomeScreenFactoryImpl

class HomeDepsProvider: HomeDeps {
    override val homeScreenFactory: HomeScreenFactory by lazy { HomeScreenFactoryImpl() }
}