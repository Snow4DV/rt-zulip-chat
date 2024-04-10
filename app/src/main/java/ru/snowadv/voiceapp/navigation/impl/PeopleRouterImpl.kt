package ru.snowadv.voiceapp.navigation.impl

import com.github.terrakok.cicerone.Router
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.voiceapp.navigation.Screens

class PeopleRouterImpl(private val ciceroneRouter: Router): PeopleRouter {
    override fun openProfile(userId: Long) {
        ciceroneRouter.navigateTo(Screens.Profile(userId, false))
    }
}