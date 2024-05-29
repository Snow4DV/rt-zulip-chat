package ru.snowadv.home_presentation.home.elm

import ru.snowadv.home_presentation.model.InnerHomeScreen

data class HomeStateElm(
    val currentScreen: InnerHomeScreen = InnerHomeScreen.CHANNELS,
)