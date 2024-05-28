package ru.snowadv.profile_presentation.ui.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.profile_presentation.ui.ProfileFragment
import ru.snowadv.profile_presentation.api.ProfileScreenFactory
import javax.inject.Inject

@Reusable
internal class ProfileScreenFactoryImpl @Inject constructor(): ProfileScreenFactory {
    override fun create(profileId: Long?): Fragment = ProfileFragment.newInstance(profileId)
}