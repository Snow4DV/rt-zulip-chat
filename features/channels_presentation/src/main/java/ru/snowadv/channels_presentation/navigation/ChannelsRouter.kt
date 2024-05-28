package ru.snowadv.channels_presentation.navigation

interface ChannelsRouter {
    fun openTopic(streamName: String, topicName: String)
}