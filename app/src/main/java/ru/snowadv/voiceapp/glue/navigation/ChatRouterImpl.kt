package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class ChatRouterImpl @Inject constructor(private val ciceroneRouter: Router):
    ru.snowadv.chat_api.domain.navigation.ChatRouter {
    override fun goBack() {
        ciceroneRouter.exit()
    }

    override fun openProfile(profileId: Long) {
        ciceroneRouter.navigateTo(Screens.Profile(profileId))
    }


}