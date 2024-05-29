package ru.snowadv.chatapp.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import dagger.Reusable
import ru.snowadv.auth_presentation.di.holder.AuthPresentationComponentHolder
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationComponentHolder
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.home_presentation.di.dagger.HomePresentationComponentHolder
import ru.snowadv.people_presentation.di.holder.PeoplePresentationComponentHolder
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationComponentHolder
import javax.inject.Inject

@Reusable
class Screens @Inject constructor(){
    fun Channels() = FragmentScreen { ChannelsPresentationComponentHolder.get().screenFactory.create() }
    fun Chat(streamId: Long, streamName: String, topicName: String?) = FragmentScreen { ChatPresentationComponentHolder.get().screenFactory.create(
        streamName = streamName,
        topicName = topicName,
        streamId = streamId,
    ) }
    fun People() = FragmentScreen { PeoplePresentationComponentHolder.get().screenFactory.create() }
    fun Profile(userId: Long?) = FragmentScreen { ProfilePresentationComponentHolder.get().screenFactory.create(profileId = userId) }
    fun Home() = FragmentScreen { HomePresentationComponentHolder.get().screenFactory.create() }
    fun Login() = FragmentScreen { AuthPresentationComponentHolder.get().screenFactory.create() }
}