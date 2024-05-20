package ru.snowadv.chatapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.chatapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class ChannelsRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    ChannelsRouter {
    override fun openTopic(streamName: String, topicName: String) {
        ciceroneRouter.navigateTo(screens.Chat(streamName, topicName))
    }


}