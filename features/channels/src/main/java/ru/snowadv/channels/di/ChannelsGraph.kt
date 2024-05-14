package ru.snowadv.channels.di

import ru.snowadv.channels.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels.domain.use_case.GetTopicsUseCase
import ru.snowadv.channels.domain.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListActorElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListStoreFactoryElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListActorElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListStoreFactoryElm

object ChannelsGraph {
    internal lateinit var deps: ChannelsDeps
    internal val getStreamsUseCase by lazy { GetStreamsUseCase(deps.streamRepo) }
    internal val getTopicsUseCase by lazy { GetTopicsUseCase(deps.topicRepo) }
    internal val listenToStreamEventsUseCase by lazy { ListenToStreamEventsUseCase(deps.eventRepo) }
    internal val channelListActorElm by lazy { ChannelListActorElm() }
    internal val streamListActorElm by lazy { StreamListActorElm(deps.router, getStreamsUseCase, getTopicsUseCase, listenToStreamEventsUseCase) }

    fun init(deps: ChannelsDeps) {
        this.deps = deps
    }
}