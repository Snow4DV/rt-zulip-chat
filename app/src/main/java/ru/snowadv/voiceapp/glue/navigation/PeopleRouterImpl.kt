package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class PeopleRouterImpl @Inject constructor(private val ciceroneRouter: Router): PeopleRouter {
    override fun openProfile(userId: Long) {
        ciceroneRouter.navigateTo(Screens.Profile(userId))
    }
}