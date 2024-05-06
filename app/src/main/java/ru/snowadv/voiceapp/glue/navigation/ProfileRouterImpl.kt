package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import javax.inject.Inject

@Reusable
internal class ProfileRouterImpl @Inject constructor(private val ciceroneRouter: Router):
    ProfileRouter {

    override fun goBack() {
        ciceroneRouter.exit()
    }
}