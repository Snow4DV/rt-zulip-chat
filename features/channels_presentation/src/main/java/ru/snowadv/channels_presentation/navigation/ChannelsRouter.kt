package ru.snowadv.channels_presentation.navigation

interface ChannelsRouter {
    fun openTopic(streamId: Long, streamName: String, topicName: String)
    fun openStream(streamId: Long, streamName: String)
}