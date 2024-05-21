package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class ProfileRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    ProfileRouter {

    override fun goBack() {
        ciceroneRouter.exit()
    }

    override fun logOut() {
        ciceroneRouter.newRootScreen(screens.Login())
    }
}