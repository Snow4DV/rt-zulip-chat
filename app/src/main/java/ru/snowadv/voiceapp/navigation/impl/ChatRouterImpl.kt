package ru.snowadv.voiceapp.navigation.impl

import com.github.terrakok.cicerone.Router
import ru.snowadv.chat.presentation.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens

class ChatRouterImpl(private val ciceroneRouter: Router): ChatRouter {
    override fun goBack() {
        ciceroneRouter.exit()
    }

    override fun openProfile(profileId: Long) {
        ciceroneRouter.navigateTo(Screens.Profile(profileId, false))
    }


}