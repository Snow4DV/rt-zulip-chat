package ru.snowadv.home.presentation.home.event

import ru.snowadv.home.presentation.local_navigation.HomeScreen

sealed class HomeScreenEvent {
    class OnBottomTabSelected(val screen: HomeScreen): HomeScreenEvent()
}