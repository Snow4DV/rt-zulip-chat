package ru.snowadv.home.presentation.local_navigation

import ru.snowadv.home.R

enum class HomeScreen(val titleResId: Int, val iconResId: Int, val menuActionId: Int, val tag: String) {
    CHANNELS(R.string.channels, R.drawable.ic_channels, R.id.action_channels, "channels_fragment"),
    PEOPLE(R.string.people, R.drawable.ic_people, R.id.action_people, "people_fragment"),
    PROFILE(R.string.profile, R.drawable.ic_profile, R.id.action_profile, "profile_fragment");

    companion object {
        fun getByActionId(actionId: Int): HomeScreen {
            return entries.first { it.menuActionId == actionId }
        }
    }
}