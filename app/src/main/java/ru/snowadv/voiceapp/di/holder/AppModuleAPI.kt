package ru.snowadv.voiceapp.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.home_api.presentation.local_navigation.HomeScreenFactory
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.people_api.domain.repository.PeopleRepository
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_api.domain.repository.ProfileRepository

interface AppModuleAPI : BaseModuleAPI {
    val authProvider: AuthProvider
    val badAuthBehavior: BadAuthBehavior

    val channelsRouter: ChannelsRouter
    val chatRouter: ru.snowadv.chat_api.domain.navigation.ChatRouter
    val homeScreenFactory: ru.snowadv.home_api.presentation.local_navigation.HomeScreenFactory
    val peopleRouter: ru.snowadv.people_api.domain.navigation.PeopleRouter
    val profileRouter: ru.snowadv.profile_api.domain.navigation.ProfileRouter

    val streamRepo: StreamRepository
    val topicRepo: TopicRepository
    val peopleRepo: ru.snowadv.people_api.domain.repository.PeopleRepository
    val profileRepo: ru.snowadv.profile_api.domain.repository.ProfileRepository

    val json: Json
    val dispatcherProvider: DispatcherProvider
}