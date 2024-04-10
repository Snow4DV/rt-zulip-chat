package ru.snowadv.home.presentation.home.state

import ru.snowadv.home.presentation.local_navigation.HomeScreen

data class HomeScreenState(
    val currentScreen: HomeScreen = HomeScreen.CHANNELS,
)