package ru.snowadv.voiceapp.navigation.impl

import com.github.terrakok.cicerone.Router
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.voiceapp.navigation.Screens

class ProfileRouterImpl(private val ciceroneRouter: Router): ProfileRouter {

    override fun goBack() {
        ciceroneRouter.exit()
    }
}