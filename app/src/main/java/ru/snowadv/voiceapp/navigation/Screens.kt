package ru.snowadv.voiceapp.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.snowadv.channels.presentation.channel_list.ChannelListFragment
import ru.snowadv.chat.presentation.chat.ChatFragment
import ru.snowadv.home.presentation.home.HomeFragment
import ru.snowadv.people.presentation.people_list.PeopleFragment
import ru.snowadv.profile.presentation.profile.ProfileFragment

object Screens {
    fun Channels() = FragmentScreen { ChannelListFragment.newInstance() }
    fun Chat(streamName: String, topicName: String) = FragmentScreen { ChatFragment.newInstance(streamName, topicName) }
    fun People() = FragmentScreen { PeopleFragment.newInstance() }
    fun Profile(userId: Long?) = FragmentScreen { ProfileFragment.newInstance(userId)}
    fun Home() = FragmentScreen { HomeFragment.newInstance() }
}