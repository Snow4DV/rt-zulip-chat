package ru.snowadv.voiceapp.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.snowadv.channels.presentation.channel_list.ChannelListFragment
import ru.snowadv.chat.presentation.feature.ChatFeatureScreens
import ru.snowadv.home.presentation.home.HomeFragment
import ru.snowadv.people.presentation.feature.PeopleFeatureScreens
import ru.snowadv.profile.presentation.feature.ProfileFeatureScreens

object Screens {
    fun Channels() = FragmentScreen { ChannelListFragment.newInstance() }
    fun Chat(streamName: String, topicName: String) = FragmentScreen { ChatFeatureScreens.Chat(streamName, topicName) }
    fun People() = FragmentScreen { PeopleFeatureScreens.People() }
    fun Profile(userId: Long?) = FragmentScreen { ProfileFeatureScreens.Profile(userId) }
    fun Home() = FragmentScreen { HomeFragment.newInstance() }
}