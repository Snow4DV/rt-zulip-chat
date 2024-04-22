package ru.snowadv.profile.presentation.feature

import androidx.fragment.app.Fragment
import ru.snowadv.profile.presentation.profile.ProfileFragment

object ProfileFeatureScreens {
    fun Profile(userId: Long? = null): Fragment {
        return ProfileFragment.newInstance(userId)
    }
}