package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens

class ChatRouterImpl(private val ciceroneRouter: Router): ChatRouter {
    override fun goBack() {
        ciceroneRouter.exit()
    }

    override fun openProfile(profileId: Long) {
        ciceroneRouter.navigateTo(Screens.Profile(profileId, false))
    }


}