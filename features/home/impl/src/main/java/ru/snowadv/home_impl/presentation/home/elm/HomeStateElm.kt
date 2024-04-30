package ru.snowadv.home_impl.presentation.home.elm

internal data class HomeStateElm(
    val currentScreen: ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreen = ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreen.CHANNELS,
)