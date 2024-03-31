package ru.snowadv.voiceapp.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.snowadv.chat.presentation.chat.ChatFragment
import ru.snowadv.home.presentation.people_list.PeopleFragment
import ru.snowadv.home.presentation.profile.ProfileFragment
import ru.snowadv.voiceapp.di.Graph

object Screens {
    fun Chat(streamId: Long, topicName: String) = FragmentScreen { ChatFragment.newInstance(streamId, topicName) }
    fun Profile(userId: Long, isOwner: Boolean) = FragmentScreen { ProfileFragment.newInstance(userId, isOwner)}
    fun Home() = FragmentScreen { TODO() }
    fun People() = FragmentScreen { PeopleFragment.newInstance() }
}