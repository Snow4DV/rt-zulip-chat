package ru.snowadv.channels.di

import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository

interface ChannelsDeps {
    val router: ChannelsRouter
    val streamRepo: StreamRepository
    val topicRepo: TopicRepository
}