package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.auth_api.domain.navigation.AuthRouter
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class AuthRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    AuthRouter {
    override fun goToHomeAfterAuth() {
        ciceroneRouter.newRootScreen(screens.Home())
    }
}