package ru.snowadv.voiceapp.di.deps

import ru.snowadv.channels.di.ChannelsDeps
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.repository.StreamRepository
import ru.snowadv.channels.domain.repository.TopicRepository
import ru.snowadv.voiceapp.di.MainGraph
import ru.snowadv.voiceapp.glue.navigation.ChannelsRouterImpl
import ru.snowadv.voiceapp.glue.repository.ChannelsRepositoryImpl

class ChannelsDepsProvider : ChannelsDeps {
    private val channelsRepo by lazy {
        ChannelsRepositoryImpl(
            MainGraph.mainDepsProvider.streamDataRepository,
            MainGraph.mainDepsProvider.topicDataRepository
        )
    }
    override val router: ChannelsRouter by lazy { ChannelsRouterImpl(MainGraph.mainDepsProvider.router) }
    override val streamRepo: StreamRepository get() = channelsRepo
    override val topicRepo: TopicRepository get() = channelsRepo
}