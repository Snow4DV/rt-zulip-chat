package ru.snowadv.chatapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.auth_presentation.navigation.AuthRouter
import ru.snowadv.chatapp.activity.MainActivity
import ru.snowadv.chatapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class AuthRouterImpl @Inject constructor(
    private val ciceroneRouter: Router, private val screens: Screens,
):
    AuthRouter {
    override fun goToHomeAfterAuth() {
        ciceroneRouter.newRootScreen(screens.Home())
    }
}