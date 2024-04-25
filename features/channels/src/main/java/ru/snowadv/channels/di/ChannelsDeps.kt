package ru.snowadv.channels.di

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.event_api.repository.EventRepository

interface ChannelsDeps {
    val router: ChannelsRouter
    val streamRepo: StreamRepository
    val topicRepo: TopicRepository
    val eventRepo: EventRepository
    val defaultDispatcher: CoroutineDispatcher
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
}