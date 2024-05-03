package ru.snowadv.voiceapp.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.channels_impl.domain.repository.StreamRepository
import ru.snowadv.channels_impl.domain.repository.TopicRepository
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.chat_impl.domain.repository.EmojiRepository
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.people_impl.domain.repository.PeopleRepository
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_impl.domain.repository.ProfileRepository

interface AppModuleAPI : BaseModuleAPI {
    val authProvider: AuthProvider
    val badAuthBehavior: BadAuthBehavior

    val channelsRouter: ChannelsRouter
    val chatRouter: ChatRouter
    val peopleRouter: PeopleRouter
    val profileRouter: ProfileRouter

    val json: Json
    val dispatcherProvider: DispatcherProvider
}