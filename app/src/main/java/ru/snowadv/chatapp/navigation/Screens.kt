package ru.snowadv.chatapp.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import dagger.Reusable
import ru.snowadv.auth_presentation.di.holder.AuthPresentationComponentHolder
import ru.snowadv.channels_impl.di.ChannelsFeatureComponentHolder
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.home_impl.di.HomeFeatureComponentHolder
import ru.snowadv.people_impl.di.PeopleFeatureComponentHolder
import ru.snowadv.profile_impl.di.ProfileFeatureComponentHolder
import javax.inject.Inject

@Reusable
class Screens @Inject constructor(){
    fun Channels() = FragmentScreen { ChannelsFeatureComponentHolder.get().screenFactory.create() }
    fun Chat(streamName: String, topicName: String) = FragmentScreen { ChatPresentationComponentHolder.get().screenFactory.create(
        streamName = streamName,
        topicName = topicName,
    ) }
    fun People() = FragmentScreen { PeopleFeatureComponentHolder.get().screenFactory.create() }
    fun Profile(userId: Long?) = FragmentScreen { ProfileFeatureComponentHolder.get().screenFactory.create(profileId = userId) }
    fun Home() = FragmentScreen { HomeFeatureComponentHolder.get().screenFactory.create() }
    fun Login() = FragmentScreen { AuthPresentationComponentHolder.get().screenFactory.create() }
}