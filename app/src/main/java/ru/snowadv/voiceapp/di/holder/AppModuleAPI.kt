package ru.snowadv.voiceapp.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.auth_presentation.navigation.AuthRouter
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.profile_api.domain.navigation.ProfileRouter

interface AppModuleAPI : BaseModuleAPI {
    val badAuthBehavior: BadAuthBehavior

    val authRouter: AuthRouter
    val channelsRouter: ChannelsRouter
    val chatRouter: ChatRouter
    val peopleRouter: PeopleRouter
    val profileRouter: ProfileRouter

    val json: Json
    val dispatcherProvider: DispatcherProvider
}