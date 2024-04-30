package ru.snowadv.voiceapp.glue.navigation

import com.github.terrakok.cicerone.Router
import dagger.Reusable
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens
import javax.inject.Inject

@Reusable
class ChannelsRouterImpl @Inject constructor(private val ciceroneRouter: Router): ChannelsRouter {
    override fun openTopic(streamName: String, topicName: String) {
        ciceroneRouter.navigateTo(Screens.Chat(streamName, topicName))
    }


}