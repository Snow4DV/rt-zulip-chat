package ru.snowadv.home.presentation.profile.event

sealed class ProfileEvent {
    data object ClickedOnRetry: ProfileEvent()
    data object ClickedOnLogout: ProfileEvent()
    data object ClickedOnBack: ProfileEvent()
}