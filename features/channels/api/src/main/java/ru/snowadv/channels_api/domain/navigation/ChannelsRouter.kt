package ru.snowadv.channels_api.domain.navigation

interface ChannelsRouter {
    fun openTopic(streamName: String, topicName: String)
}