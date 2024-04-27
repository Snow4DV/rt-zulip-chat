package ru.snowadv.voiceapp.di.holder

import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.home.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository

interface AppModuleAPI : BaseModuleAPI {
    val authProvider: AuthProvider

    val channelsRouter: ChannelsRouter
    val chatRouter: ChatRouter
    val homeScreenFactory: HomeScreenFactory
    val peopleRouter: PeopleRouter
    val profileRouter: ProfileRouter

    val streamRepo: StreamRepository
    val topicRepo: TopicRepository
    val peopleRepo: PeopleRepository
    val profileRepo: ProfileRepository
}