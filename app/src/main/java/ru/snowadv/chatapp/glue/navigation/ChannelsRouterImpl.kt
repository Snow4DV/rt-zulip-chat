package ru.snowadv.chatapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.chatapp.navigation.Screens
import javax.inject.Inject

@Reusable
internal class ChannelsRouterImpl @Inject constructor(private val ciceroneRouter: Router, private val screens: Screens):
    ChannelsRouter {
    override fun openTopic(streamId: Long, streamName: String, topicName: String) {
        ciceroneRouter.navigateTo(screens.Chat(
            streamId = streamId,
            streamName = streamName,
            topicName = topicName,
        ))
    }

    override fun openStream(streamId: Long, streamName: String) {
        ciceroneRouter.navigateTo(screens.Chat(
            streamId = streamId,
            streamName = streamName,
            topicName = null,
        ))
    }


}