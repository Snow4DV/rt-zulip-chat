package ru.snowadv.home.presentation.profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.home.presentation.navigation.HomeRouter

internal class ProfileViewModelFactory(
    private val router: HomeRouter,
    private val profileId: Long,
    private val isOwner: Boolean,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                router = router,
                profileId = profileId,
                isOwner = isOwner,
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}