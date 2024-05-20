package ru.snowadv.home_presentation.model

import ru.snowadv.home_presentation.R


enum class InnerHomeScreen(val titleResId: Int, val iconResId: Int, val menuActionId: Int, val tag: String) {
    CHANNELS(R.string.channels, R.drawable.ic_channels, R.id.action_channels, "channels_fragment"),
    PEOPLE(R.string.people, R.drawable.ic_people, R.id.action_people, "people_fragment"),
    PROFILE(R.string.profile, R.drawable.ic_profile, R.id.action_profile, "profile_fragment");

    companion object {
        fun getByActionId(actionId: Int): InnerHomeScreen {
            return entries.first { it.menuActionId == actionId }
        }
    }
}