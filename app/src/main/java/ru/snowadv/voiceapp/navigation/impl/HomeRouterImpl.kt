package ru.snowadv.voiceapp.navigation.impl

import com.github.terrakok.cicerone.Router
import ru.snowadv.chat.presentation.navigation.ChatRouter
import ru.snowadv.home.presentation.navigation.HomeRouter
import ru.snowadv.voiceapp.navigation.Screens

class HomeRouterImpl(private val ciceroneRouter: Router): HomeRouter {
    override fun goBack() {
        ciceroneRouter.exit()
    }

    override fun openTopic(streamId: Long, topicName: String) {
        ciceroneRouter.navigateTo(Screens.Chat(streamId, topicName))
    }

    override fun openProfile(userId: Long) {
        ciceroneRouter.navigateTo(Screens.Profile(userId, false))
    }


}