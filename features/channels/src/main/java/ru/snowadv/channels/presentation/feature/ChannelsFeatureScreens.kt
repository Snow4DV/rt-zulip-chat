package ru.snowadv.channels.presentation.feature

import androidx.fragment.app.Fragment
import ru.snowadv.channels.presentation.channel_list.ChannelListFragment

object ChannelsFeatureScreens {
    fun Channels(): Fragment {
        return ChannelListFragment.newInstance()
    }
}