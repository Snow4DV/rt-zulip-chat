package ru.snowadv.voiceapp.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import dagger.Reusable
import ru.snowadv.auth_impl.di.AuthFeatureComponentHolder
import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.channels_impl.di.ChannelsFeatureComponentHolder
import ru.snowadv.chat_api.presentation.ChatScreenFactory
import ru.snowadv.chat_impl.di.ChatFeatureComponentHolder
import ru.snowadv.home_api.presentation.feature.HomeScreenFactory
import ru.snowadv.home_impl.di.HomeFeatureComponentHolder
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.people_impl.di.PeopleFeatureComponentHolder
import ru.snowadv.profile_api.presentation.ProfileScreenFactory
import ru.snowadv.profile_impl.di.ProfileFeatureComponentHolder
import javax.inject.Inject

@Reusable
class Screens @Inject constructor(){
    fun Channels() = FragmentScreen { ChannelsFeatureComponentHolder.get().screenFactory.create() }
    fun Chat(streamName: String, topicName: String) = FragmentScreen { ChatFeatureComponentHolder.get().screenFactory.create(
        streamName = streamName,
        topicName = topicName,
    ) }
    fun People() = FragmentScreen { PeopleFeatureComponentHolder.get().screenFactory.create() }
    fun Profile(userId: Long?) = FragmentScreen { ProfileFeatureComponentHolder.get().screenFactory.create(profileId = userId) }
    fun Home() = FragmentScreen { HomeFeatureComponentHolder.get().screenFactory.create() }
    fun Login() = FragmentScreen { AuthFeatureComponentHolder.get().screenFactory.create() }
}