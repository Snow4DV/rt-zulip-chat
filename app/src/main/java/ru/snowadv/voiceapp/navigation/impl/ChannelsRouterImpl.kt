package ru.snowadv.voiceapp.navigation.impl

import com.github.terrakok.cicerone.Router
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.chat.domain.navigation.ChatRouter
import ru.snowadv.voiceapp.navigation.Screens

class ChannelsRouterImpl(private val ciceroneRouter: Router): ChannelsRouter {
    override fun openTopic(streamName: String, topicName: String) {
        ciceroneRouter.navigateTo(Screens.Chat(streamName, topicName))
    }


}