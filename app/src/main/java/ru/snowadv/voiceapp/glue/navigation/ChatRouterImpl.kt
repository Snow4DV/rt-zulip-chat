package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class ChatRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    ChatRouter {
    override fun goBack() {
        ciceroneRouter.exit()
    }

    override fun openProfile(profileId: Long) {
        ciceroneRouter.navigateTo(screens.Profile(profileId))
    }


}