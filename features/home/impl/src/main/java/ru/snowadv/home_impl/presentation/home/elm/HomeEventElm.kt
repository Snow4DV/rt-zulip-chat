package ru.snowadv.home_impl.presentation.home.elm

internal sealed interface HomeEventElm {

    sealed interface Ui : HomeEventElm {
        data class OnBottomTabSelected(val screen: ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreen) :
            Ui
    }

    sealed interface Internal : HomeEventElm

}