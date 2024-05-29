package ru.snowadv.home_presentation.home.elm

import ru.snowadv.home_presentation.model.InnerHomeScreen

sealed interface HomeEventElm {

    sealed interface Ui : HomeEventElm {
        data class OnBottomTabSelected(val screen: InnerHomeScreen) :
            Ui
    }

    sealed interface Internal : HomeEventElm

}