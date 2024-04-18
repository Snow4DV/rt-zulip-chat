package ru.snowadv.channels.di

import ru.snowadv.channels.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels.domain.use_case.GetTopicsUseCase

object ChannelsGraph {
    internal lateinit var deps: ChannelsDeps
    internal val getStreamsUseCase by lazy { GetStreamsUseCase(deps.streamRepo) }
    internal val getTopicsUseCase by lazy { GetTopicsUseCase(deps.topicRepo) }

    fun init(deps: ChannelsDeps) {
        this.deps = deps
    }
}