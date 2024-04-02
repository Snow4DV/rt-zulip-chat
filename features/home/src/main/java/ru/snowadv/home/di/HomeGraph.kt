package ru.snowadv.home.di

import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory

object HomeGraph {
    internal lateinit var homeScreenFactory: HomeScreenFactory

    fun init(homeScreenFactory: HomeScreenFactory) {
        this.homeScreenFactory = homeScreenFactory
    }
}