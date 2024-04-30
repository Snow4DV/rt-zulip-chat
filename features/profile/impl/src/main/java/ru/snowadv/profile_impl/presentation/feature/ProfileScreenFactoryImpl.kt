package ru.snowadv.profile_impl.presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.profile_impl.presentation.profile.ProfileFragment
import ru.snowadv.profile_api.presentation.ProfileScreenFactory
import javax.inject.Inject

@Reusable
class ProfileScreenFactoryImpl @Inject constructor(): ProfileScreenFactory {
    override fun create(profileId: Long?): Fragment = ProfileFragment.newInstance(profileId)
}