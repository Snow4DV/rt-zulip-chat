package ru.snowadv.channels_presentation.stream_list.ui.elm

import dagger.Reusable
import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamTopics
import ru.snowadv.channels_domain_api.model.StreamUnreadMessages
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEffectElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEventElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListStateElm
import ru.snowadv.channels_presentation.stream_list.ui.model.UiShimmerTopic
import ru.snowadv.channels_presentation.stream_list.ui.util.StreamsMapper.toDomainModel
import ru.snowadv.channels_presentation.stream_list.ui.util.StreamsMapper.toUiModel
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.elm.ElmMapper
import javax.inject.Inject

@Reusable
internal class StreamListElmMapper @Inject constructor() :
    ElmMapper<StreamListStateElm, StreamListEffectElm, StreamListEventElm, StreamListStateUiElm, StreamListEffectUiElm, StreamListEventUiElm> {
    override fun mapState(state: StreamListStateElm): StreamListStateUiElm {
        return StreamListStateUiElm(
            screenState = combineStreamsAndTopics(
                streamsScreenState = state.screenState,
                topics = state.topics,
                streamUnreadMessages = state.streamsUnreadMessages,
            ),
            searchQuery = state.searchQuery,
        )
    }

    override fun mapEffect(effect: StreamListEffectElm): StreamListEffectUiElm = when (effect) {
        is StreamListEffectElm.ShowInternetErrorWithRetry -> StreamListEffectUiElm.ShowInternetErrorWithRetry(
            effect.retryEvent
        )
    }

    override fun mapUiEvent(uiEvent: StreamListEventUiElm): StreamListEventElm = when (uiEvent) {
        is StreamListEventUiElm.ChangedQuery -> StreamListEventElm.Ui.ChangedQuery(uiEvent.query)
        is StreamListEventUiElm.ClickedOnExpandStream -> StreamListEventElm.Ui.ClickedOnExpandStream(uiEvent.streamId)
        is StreamListEventUiElm.ClickedOnTopic -> StreamListEventElm.Ui.ClickedOnTopic(uiEvent.topicName)
        StreamListEventUiElm.Init -> StreamListEventElm.Ui.Init
        StreamListEventUiElm.Paused -> StreamListEventElm.Ui.Paused
        StreamListEventUiElm.ReloadClicked -> StreamListEventElm.Ui.ReloadClicked
        StreamListEventUiElm.Resumed -> StreamListEventElm.Ui.Resumed
        is StreamListEventUiElm.ClickedOnChangeStreamSubscriptionStatus -> {
            StreamListEventElm.Ui.ClickedOnChangeStreamSubscriptionStatus(uiEvent.uiStream.toDomainModel())
        }
        is StreamListEventUiElm.ClickedOnOpenStream -> {
            StreamListEventElm.Ui.ClickedOnOpenStream(streamName = uiEvent.streamName, streamId = uiEvent.streamId)
        }
    }

    private fun combineStreamsAndTopics(
        streamsScreenState: ScreenState<List<Stream>>,
        topics: StreamTopics?,
        streamUnreadMessages: List<StreamUnreadMessages>,
    ): ScreenState<List<DelegateItem>> {
        return streamsScreenState.map { streams ->
            buildList {
                streams.forEach { stream ->
                    val expanded = topics?.streamId == stream.id
                    add(stream.toUiModel(expanded))
                    if (topics != null && expanded) {
                        topics.topics.data?.map { topic ->
                            topic.toUiModel(
                                unreadMsgsCount = streamUnreadMessages
                                    .firstOrNull { it.streamId == stream.id }
                                    ?.topicsUnreadMessages
                                    ?.firstOrNull { it.topicName == topic.name }
                                    ?.unreadMessagesIds?.size ?: 0,
                            )
                        }?.let { addAll(it) } ?: run {
                            if (topics.topics is Resource.Loading) {
                                addAll(UiShimmerTopic.generateShimmers(streamId = topics.streamId))
                            }
                        }
                    }
                }
            }
        }
    }
}