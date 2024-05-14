package ru.snowadv.home.presentation.home.elm

import ru.snowadv.home.presentation.local_navigation.HomeScreen

internal sealed interface HomeEventElm {

    sealed interface Ui : HomeEventElm {
        data class OnBottomTabSelected(val screen: HomeScreen) : Ui
    }

    sealed interface Internal : HomeEventElm

}