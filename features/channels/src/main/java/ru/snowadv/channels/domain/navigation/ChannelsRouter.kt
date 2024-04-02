package ru.snowadv.channels.domain.navigation

interface ChannelsRouter {
    fun openTopic(streamName: String, topicName: String)
}