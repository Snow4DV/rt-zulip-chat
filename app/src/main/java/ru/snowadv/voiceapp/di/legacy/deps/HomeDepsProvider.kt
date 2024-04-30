package ru.snowadv.voiceapp.di.legacy.deps

import di.HomeDeps
import ru.snowadv.home_api.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.voiceapp.glue.navigation.HomeScreenFactoryImpl

class HomeDepsProvider: di.HomeDeps {
    override val homeScreenFactory: ru.snowadv.home_api.presentation.local_navigation.HomeScreenFactory by lazy { HomeScreenFactoryImpl() }
}