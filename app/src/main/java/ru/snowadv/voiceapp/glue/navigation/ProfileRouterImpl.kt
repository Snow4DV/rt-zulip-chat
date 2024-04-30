package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import javax.inject.Inject

@Reusable
class ProfileRouterImpl @Inject constructor(private val ciceroneRouter: Router):
    ru.snowadv.profile_api.domain.navigation.ProfileRouter {

    override fun goBack() {
        ciceroneRouter.exit()
    }
}