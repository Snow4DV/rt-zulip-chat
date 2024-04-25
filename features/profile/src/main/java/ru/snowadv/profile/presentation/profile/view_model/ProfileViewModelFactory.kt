package ru.snowadv.profile.presentation.profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.profile.domain.use_case.GetProfileUseCase
import ru.snowadv.profile.domain.use_case.ListenToPresenceEventsUseCase

internal class ProfileViewModelFactory(
    private val router: ProfileRouter,
    private val profileId: Long?,
    private val getProfileUseCase: GetProfileUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                router = router,
                profileId = profileId,
                getProfileUseCase = getProfileUseCase,
                listenToPresenceEventsUseCase = listenToPresenceEventsUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}