package ru.snowadv.channels_impl.presentation.stream_list.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.channels_impl.presentation.util.ChannelsMapper.toElmEvent
import ru.snowadv.channels_impl.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels_impl.domain.use_case.GetTopicsUseCase
import ru.snowadv.channels_impl.domain.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels_impl.presentation.util.ChannelsMapper.toUiModel
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
                        is Resource.Error -> StreamListEventElm.Internal.Error(res.throwable)
                        Resource.Loading -> StreamListEventElm.Internal.Loading
                        is Resource.Success -> StreamListEventElm.Internal.StreamsLoaded(res.data.map { it.toUiModel() })
                    }
                }
            }
            is StreamListCommandElm.LoadTopics -> {
                getTopicsUseCase(command.streamId).map { res ->
                    when(res) {
                        is Resource.Error -> StreamListEventElm.Internal.TopicsLoadingError(command.streamId, res.throwable)
                        Resource.Loading -> StreamListEventElm.Internal.TopicsLoading(command.streamId)
                        is Resource.Success -> StreamListEventElm.Internal.TopicsLoaded(command.streamId, res.data.map { it.toUiModel() })
                    }
                }
            }
            is StreamListCommandElm.ObserveEvents -> {
                listenToStreamEventsUseCase(command.isRestart, command.queueProps).map { event -> event.toElmEvent() }
            }
        }
    }
}
