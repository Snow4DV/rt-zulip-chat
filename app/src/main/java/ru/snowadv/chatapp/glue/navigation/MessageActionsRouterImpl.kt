package ru.snowadv.chatapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.chatapp.navigation.Screens
import ru.snowadv.message_actions_presentation.navigation.MessageActionsRouter
import javax.inject.Inject

@Reusable
internal class MessageActionsRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    MessageActionsRouter {
    override fun openProfile(profileId: Long) {
        ciceroneRouter.navigateTo(screens.Profile(profileId))
    }


}