package ru.snowadv.channels.di

import ru.snowadv.channels.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels.domain.use_case.GetTopicsUseCase
import ru.snowadv.channels.domain.use_case.ListenToStreamEventsUseCase

object ChannelsGraph {
    internal lateinit var deps: ChannelsDeps
    internal val getStreamsUseCase by lazy { GetStreamsUseCase(deps.streamRepo) }
    internal val getTopicsUseCase by lazy { GetTopicsUseCase(deps.topicRepo) }
    internal val listenToStreamEventsUseCase by lazy { ListenToStreamEventsUseCase(deps.eventRepo) }

    fun init(deps: ChannelsDeps) {
        this.deps = deps
    }
}