package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class PeopleRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    PeopleRouter {
    override fun openProfile(userId: Long) {
        ciceroneRouter.navigateTo(screens.Profile(userId))
    }
}