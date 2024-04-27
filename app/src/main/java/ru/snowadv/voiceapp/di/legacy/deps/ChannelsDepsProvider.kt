package ru.snowadv.voiceapp.di.legacy.deps

import kotlinx.coroutines.CoroutineDispatcher
import ru.snowadv.channels.di.ChannelsDeps
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.voiceapp.di.legacy.MainGraph
import ru.snowadv.voiceapp.glue.coroutines.DispatcherProviderImpl
import ru.snowadv.voiceapp.glue.navigation.ChannelsRouterImpl
import ru.snowadv.voiceapp.glue.repository.ChannelsRepositoryImpl

class ChannelsDepsProvider : ChannelsDeps {
    private val channelsRepo by lazy {
        ChannelsRepositoryImpl(
            MainGraph.mainDepsProvider.streamDataRepository,
            MainGraph.mainDepsProvider.topicDataRepository,
            DispatcherProviderImpl()
        )
    }
    override val router: ChannelsRouter by lazy { ChannelsRouterImpl(MainGraph.mainDepsProvider.router) }
    override val streamRepo: StreamRepository get() = channelsRepo
    override val topicRepo: TopicRepository get() = channelsRepo
    override val eventRepo: EventRepository
        get() = MainGraph.mainDepsProvider.eventDataRepository
    override val defaultDispatcher: CoroutineDispatcher
        get() = MainGraph.mainDepsProvider.defaultDispatcher
    override val ioDispatcher: CoroutineDispatcher
        get() = MainGraph.mainDepsProvider.ioDispatcher
    override val mainDispatcher: CoroutineDispatcher
        get() = MainGraph.mainDepsProvider.mainDispatcher
}