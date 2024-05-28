package ru.snowadv.channels_presentation.stream_list.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.channels_presentation.stream_list.presentation.util.StreamListElmMappers.toElmEvent
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class StreamListActorElm @Inject constructor(
    private val router: ChannelsRouter,
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val listenToStreamEventsUseCase: ListenToStreamEventsUseCase,
) : Actor<StreamListCommandElm, StreamListEventElm>() {
    override fun execute(command: StreamListCommandElm): Flow<StreamListEventElm> {
        return when(command) {
            is StreamListCommandElm.GoToChat -> flow {
                router.openTopic(command.streamName, command.topicName)
            }
            is StreamListCommandElm.LoadStreams -> {
                getStreamsUseCase(command.type).map { res ->
                    when(res) {
                        is Resource.Error -> StreamListEventElm.Internal.Error(
                            throwable = res.throwable,
                            cachedStreams = res.data,
                        )
                        is Resource.Loading -> res.data?.let {
                            StreamListEventElm.Internal.StreamsLoaded(
                                streams = it,
                                cached = true
                            )
                        } ?: StreamListEventElm.Internal.Loading
                        is Resource.Success -> StreamListEventElm.Internal.StreamsLoaded(
                            streams = res.data,
                            cached = false
                        )
                    }
                }
            }
            is StreamListCommandElm.LoadTopics -> {
                getTopicsUseCase(command.streamId).map { res ->
                    when(res) {
                        is Resource.Error -> res.data?.let { cachedData ->
                            StreamListEventElm.Internal.TopicsLoadingErrorWithCachedTopics(
                                streamId = command.streamId,
                                throwable = res.throwable,
                                cachedTopics = cachedData,
                            )
                        }
                            ?: StreamListEventElm.Internal.TopicsLoadingError(
                                streamId = command.streamId,
                                throwable = res.throwable,
                            )
                        is Resource.Loading -> res.data?.let { data ->
                            StreamListEventElm.Internal.TopicsLoaded(
                                streamId = command.streamId,
                                topics = data,
                            )
                        } ?: StreamListEventElm.Internal.TopicsLoading(command.streamId)
                        is Resource.Success -> StreamListEventElm.Internal.TopicsLoaded(
                            streamId = command.streamId,
                            topics = res.data,
                        )
                    }
                }
            }
            is StreamListCommandElm.ObserveEvents -> {
                listenToStreamEventsUseCase(command.isRestart, command.queueProps).map { event -> event.toElmEvent() }
            }
        }
    }
}
