package ru.snowadv.profile_presentation.ui.elm

internal sealed interface ProfileEventUiElm {
    data object Init : ProfileEventUiElm
    data object Paused : ProfileEventUiElm
    data object Resumed : ProfileEventUiElm
    data object ClickedOnRetry : ProfileEventUiElm
    data object ClickedOnBack : ProfileEventUiElm
    data object ClickedOnLogout : ProfileEventUiElm
}