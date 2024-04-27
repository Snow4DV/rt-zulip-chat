package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class ProfileRouterImpl @Inject constructor(private val ciceroneRouter: Router): ProfileRouter {

    override fun goBack() {
        ciceroneRouter.exit()
    }
}