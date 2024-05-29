package ru.snowadv.chatapp.di.holder

import com.github.terrakok.cicerone.Router
import kotlinx.serialization.json.Json
import ru.snowadv.auth_presentation.navigation.AuthRouter
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.chatapp.navigation.Screens
import ru.snowadv.message_actions_presentation.navigation.MessageActionsRouter
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.model.LoggerToggle
import ru.snowadv.people_presentation.navigation.PeopleRouter
import ru.snowadv.profile_presentation.navigation.ProfileRouter

interface AppModuleAPI : BaseModuleAPI {
    val badAuthBehavior: BadAuthBehavior

    val authRouter: AuthRouter
    val channelsRouter: ChannelsRouter
    val chatRouter: ChatRouter
    val peopleRouter: PeopleRouter
    val profileRouter: ProfileRouter
    val messageActionsRouter: MessageActionsRouter

    val json: Json
    val dispatcherProvider: DispatcherProvider

    val baseUrlProvider: BaseUrlProvider

    val router: Router
    val screens: Screens

    val networkLoggerToggle: LoggerToggle
}