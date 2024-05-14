package ru.snowadv.home.presentation.home.elm

import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.presentation.model.ScreenState

internal data class HomeStateElm(
    val currentScreen: HomeScreen = HomeScreen.CHANNELS,
)