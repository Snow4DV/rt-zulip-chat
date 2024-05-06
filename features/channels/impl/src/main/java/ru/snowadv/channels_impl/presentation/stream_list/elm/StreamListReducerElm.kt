package ru.snowadv.channels_impl.presentation.stream_list.elm

import ru.snowadv.channels_impl.domain.model.StreamType
import ru.snowadv.channels_impl.presentation.model.ShimmerTopic
import ru.snowadv.channels_impl.presentation.model.Stream
import ru.snowadv.channels_impl.presentation.model.StreamIdContainer
import ru.snowadv.channels_impl.presentation.model.StreamUnreadMessages
import ru.snowadv.channels_impl.presentation.model.Topic
import ru.snowadv.channels_impl.presentation.model.TopicUnreadMessages
import ru.snowadv.events_api.domain.model.EventQueueProperties
import ru.snowadv.events_api.domain.helper.StateMachineQueueHelper
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.util.filterList
import ru.snowadv.presentation.util.toScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class StreamListReducerElm @Inject constructor():
    ScreenDslReducer<StreamListEventElm, StreamListEventElm.Ui, StreamListEventElm.Internal, StreamListStateElm, StreamListEffectElm, StreamListCommandElm>(
        uiEventClass = StreamListEventElm.Ui::class,
        internalEventClass = StreamListEventElm.Internal::class,
    ) {
    override fun Result.internal(event: StreamListEventElm.Internal) {
        if (!StateMachineQueueHelper.determineIfEventIsByServerAndBelongsToStateOrOther(
                queueProps = state.eventQueueData, event = event
            )
        ) return
        when (event) {
            is StreamListEventElm.Internal.Error -> state {
                copy(
                    screenState = ScreenState.Error(event.throwable, event.cachedStreams),
                    screenUnfilteredDataRes = Resource.Error(event.throwable, event.cachedStreams),
                    eventQueueData = null,
                )
            }

            StreamListEventElm.Internal.Loading -> state {
                copy(
                    screenState = ScreenState.Loading(),
                    screenUnfilteredDataRes = Resource.Loading(),
                    eventQueueData = null,
                )
            }

            is StreamListEventElm.Internal.ServerEvent.EventQueueFailed -> {
                commands {
                    if (event.recreateQueue) {
                        +StreamListCommandElm.LoadStreams(state.streamType)
                    } else if (state.resumed) {
                        +StreamListCommandElm.ObserveEvents(
                            isRestart = true,
                            queueProps = state.eventQueueData,
                        )
                    }
                }
            }

            is StreamListEventElm.Internal.ServerEvent.EventQueueRegistered -> {
                state {
                    setInitialUnreadMessages(
                        streamsUnreadMessages = event.streamsUnreadMessages,
                        eventId = event.eventId,
                        queueId = event.queueId,
                        timeoutSeconds = event.timeoutSeconds
                    )
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.EventQueueUpdated -> {
                state {
                    copy(
                        eventQueueData = eventQueueData?.copy(lastEventId = event.eventId),
                    )
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.MessageDeleted -> {
                state {
                    markMessagesAsRead(listOf(event.messageId), event.eventId)
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.MessagesRead -> {
                state {
                    markMessagesAsRead(event.readMessagesIds, event.eventId)
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.MessagesUnread -> {
                state {
                    markMessagesAsUnread(
                        newUnreadMessages = event.nowUnreadMessages,
                        eventId = event.eventId,
                    )
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.NewMessage -> {
                state {
                    if (event.streamId != null && "read" !in event.flags) {
                        addNewMessage(event.streamId, event.topicName, event.messageId, event.eventId)
                    } else {
                        copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                    }
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.StreamUpdatedEvent -> {
                state {
                    event.streamId?.let { streamId ->
                        event.streamName?.let { streamName ->
                            updateStream(streamId, streamName, event.eventId)
                        }
                    } ?: copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.StreamsAddedEvent -> {
                state {
                    if (state.streamType == StreamType.ALL) {
                        addStreams(event.streams, event.eventId)
                    } else {
                        copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                    }
                }
                commandObserve()
            }
            is StreamListEventElm.Internal.ServerEvent.StreamsRemovedEvent -> {
                state {
                    removeStreams(event.streams, event.eventId)
                }
                commandObserve()
            }
            is StreamListEventElm.Internal.ServerEvent.UserSubscriptionsAddedEvent -> {
                state {
                    if (state.streamType == StreamType.SUBSCRIBED) {
                        addStreams(event.changedStreams, event.eventId)
                    } else {
                        copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                    }
                }
                commandObserve()
            }
            is StreamListEventElm.Internal.ServerEvent.UserSubscriptionsRemovedEvent -> {
                state {
                    if (state.streamType == StreamType.SUBSCRIBED) {
                        addStreams(event.changedStreams, event.eventId)
                    } else {
                        copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                    }
                }
                commandObserve()
            }
            is StreamListEventElm.Internal.StreamsLoaded -> {
                state {
                    val newData = if (event.cached) Resource.Success(event.streams) else Resource.Success(event.streams)
                    copy(
                        screenUnfilteredDataRes = newData,
                        screenState = newData.toScreenState(),
                        eventQueueData = null,
                    )
                }
                if (!event.cached) {
                    commandObserve()
                }
            }

            is StreamListEventElm.Internal.TopicsLoaded -> state {
                loadTopics(event.streamId, event.topics)
            }

            is StreamListEventElm.Internal.TopicsLoading -> state {
                loadTopics(event.streamId, ShimmerTopic.generateShimmers(event.streamId))
            }

            is StreamListEventElm.Internal.TopicsLoadingErrorWithCachedTopics -> {
                state {
                    loadTopics(event.streamId, event.cachedTopics)
                }
            }

            is StreamListEventElm.Internal.TopicsLoadingError -> {
                state {
                    hideTopics()
                }
                effects {
                    +StreamListEffectElm.ShowInternetErrorWithRetry(
                        StreamListEventElm.Ui.ClickedOnStream(
                            event.streamId
                        )
                    )
                }
            }
        }
    }

    override fun Result.ui(event: StreamListEventElm.Ui) {
        when(event) {
            is StreamListEventElm.Ui.ClickedOnTopic -> {
                state.selectedStream?.name?.let { streamName ->
                    commands {
                        +StreamListCommandElm.GoToChat(streamName, event.topicName)
                    }
                }
            }
            StreamListEventElm.Ui.Init, StreamListEventElm.Ui.ReloadClicked -> commands {
                +StreamListCommandElm.LoadStreams(state.streamType)
            }
            is StreamListEventElm.Ui.ClickedOnStream -> {
                if (state.selectedStream?.id == event.streamId) {
                    state { hideTopics() }
                } else {
                    commands {
                        +StreamListCommandElm.LoadTopics(event.streamId)
                    }
                }
            }
            StreamListEventElm.Ui.Paused -> state {
                copy(resumed = false)
            }
            StreamListEventElm.Ui.Resumed -> {
                state {
                    copy(resumed = true)
                }
                commandObserve()
            }

            is StreamListEventElm.Ui.ChangedQuery -> if (state.searchQuery != event.query) {
                state {
                    copy(
                        searchQuery = event.query,
                        screenState = screenUnfilteredDataRes.toScreenState().filterStreamsByQuery(event.query),
                    )
                }
            }
        }
    }


    private fun Result.commandObserve() {
        if (!state.resumed || state.screenState !is ScreenState.Success) return
        commands {
            +StreamListCommandElm.ObserveEvents(
                isRestart = false,
                queueProps = state.eventQueueData,
            )
        }
    }

    private fun StreamListStateElm.loadTopics(
        streamId: Long,
        topics: List<DelegateItem>,
    ): StreamListStateElm {
        val newData = screenUnfilteredDataRes.map { oldList ->
            buildList {
                oldList.filterIsInstance<Stream>().forEach { item ->
                    val shouldBeExpanded = (item.id == streamId)
                    val newItem = if (item.expanded != shouldBeExpanded) {
                        item.copy(expanded = shouldBeExpanded)
                    } else {
                        item
                    }
                    add(newItem)
                    if (shouldBeExpanded) {
                        addAll(topics)
                    }
                }
            }.setStreamUnreadMessages(streamsUnreadMessages)
        }
        return copy(
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
        )
    }

    private fun StreamListStateElm.addStreams(
        streams: List<Stream>,
        eventId: Long
    ): StreamListStateElm {
        val streamIds = screenState.getCurrentData()
            ?.asSequence()
            ?.mapNotNull { if (it is Stream) it.id else null }
            ?.toSet() ?: emptySet()
        val newData =
            screenUnfilteredDataRes.map { it + streams.filter { stream -> stream.id !in streamIds } }
        return copy(
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.removeStreams(
        streams: List<Stream>,
        eventId: Long,
    ): StreamListStateElm {
        val removedStreamIds = streams.asSequence().map { it.id }.toSet()
        val curState = if (selectedStream?.id?.let { id -> id in removedStreamIds } == true) {
            this.hideTopics()
        } else {
            this
        }
        return curState.copy(
            screenState = screenState.filterList { !(it is Stream && it.id in removedStreamIds) },
            screenUnfilteredDataRes = screenUnfilteredDataRes.map { list ->
                list.filter {
                    !(it is Stream && it.id in removedStreamIds)
                }
            },
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.updateStream(
        streamId: Long,
        streamName: String,
        eventId: Long,
    ): StreamListStateElm {
        val newData = screenUnfilteredDataRes.map {
            it.map { delegateItem ->
                if (delegateItem is Stream && delegateItem.id == streamId) {
                    delegateItem.copy(
                        name = streamName
                    )
                } else {
                    delegateItem
                }
            }
        }
        return copy(
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.hideTopics(): StreamListStateElm {
        val newData = screenUnfilteredDataRes.map { list ->
            list.mapNotNull {delegateItem ->
                if (delegateItem is Stream && delegateItem.expanded) {
                    delegateItem.copy(expanded = false)
                } else if (delegateItem is Stream) {
                    delegateItem
                } else {
                    null
                }
            }
        }
        return copy(
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
        )
    }

    private fun ScreenState<List<DelegateItem>>.filterStreamsByQuery(searchQuery: String): ScreenState<List<DelegateItem>> {
        val filteredStreamIds = getCurrentData()
            ?.asSequence()
            ?.mapNotNull {
                if (it is Stream && it.name.contains(
                        other = searchQuery,
                        ignoreCase = true
                    )
                ) it.id else null
            }
            ?.toSet() ?: emptySet()
        return filterList {
            it is Stream && it.id in filteredStreamIds
                    || it is StreamIdContainer && it.streamId in filteredStreamIds
        }
    }

    private fun StreamListStateElm.setInitialUnreadMessages(
        streamsUnreadMessages: List<StreamUnreadMessages>,
        eventId: Long,
        queueId: String,
        timeoutSeconds: Int,
    ): StreamListStateElm {
        return copy(
            streamsUnreadMessages = streamsUnreadMessages,
            screenState = screenState.map { it.setStreamUnreadMessages(streamsUnreadMessages) },
            screenUnfilteredDataRes = screenUnfilteredDataRes.map {
                it.setStreamUnreadMessages(
                    streamsUnreadMessages
                )
            },
            eventQueueData = EventQueueProperties(queueId = queueId, timeoutSeconds = timeoutSeconds, lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.addNewMessage(
        streamId: Long,
        topicName: String,
        messageId: Long,
        eventId: Long,
    ): StreamListStateElm {
        return markMessagesAsUnread(
            newUnreadMessages = listOf(
                StreamUnreadMessages(streamId, listOf(TopicUnreadMessages(topicName, listOf(messageId))))
            ),
            eventId = eventId,
        )
    }

    private fun StreamListStateElm.markMessagesAsRead(
        messagesIdsList: List<Long>,
        eventId: Long,
    ): StreamListStateElm {
        val messagesIds = messagesIdsList.toSet()
        val newStreamsUnreadMessages = streamsUnreadMessages.map { streamUnreadMessages ->
            streamUnreadMessages.copy(
                topicsUnreadMessages = streamUnreadMessages.topicsUnreadMessages.map { topicUnreadMessages ->
                    topicUnreadMessages.copy(
                        unreadMessagesIds = topicUnreadMessages.unreadMessagesIds
                            .filter { messagesId -> messagesId !in messagesIds }
                    )
                }
            )
        }
        val newData = screenUnfilteredDataRes.map {
            it.setStreamUnreadMessages(newStreamsUnreadMessages)
        }
        return copy(
            streamsUnreadMessages = newStreamsUnreadMessages,
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.markMessagesAsUnread(
        newUnreadMessages: List<StreamUnreadMessages>,
        eventId: Long
    ): StreamListStateElm {
        val oldUnreadMessagesByStreamIdAndTopicName = streamsUnreadMessages.groupBy { it.streamId }
            .mapValues {
                it.value.flatMap { streamsUnreadMessages -> streamsUnreadMessages.topicsUnreadMessages }
                    .associateBy { topicUnreadMessages -> topicUnreadMessages.topicName }
            }
        val newUnreadMessagesByStreamIdAndTopicName = newUnreadMessages.groupBy { it.streamId }
            .mapValues {
                it.value.flatMap { streamsUnreadMessages -> streamsUnreadMessages.topicsUnreadMessages }
                    .associateBy { topicUnreadMessages -> topicUnreadMessages.topicName }
            }
        val newStreamsUnreadMessages = buildList {
            // Update previous object if it exists already
            streamsUnreadMessages.map { singleStreamUnreadMessages ->
                singleStreamUnreadMessages.copy(
                    topicsUnreadMessages = buildList {
                        // Map topic messages that exist already
                        singleStreamUnreadMessages.topicsUnreadMessages.map { topicUnreadMessages ->
                            topicUnreadMessages.copy(
                                unreadMessagesIds = buildSet {
                                    addAll(topicUnreadMessages.unreadMessagesIds)
                                    newUnreadMessagesByStreamIdAndTopicName[singleStreamUnreadMessages.streamId]
                                        ?.get(topicUnreadMessages.topicName)
                                        ?.let { prevTopicUnreadMessages ->
                                            addAll(prevTopicUnreadMessages.unreadMessagesIds.toSet())
                                        }
                                }.toList()
                            )
                        }.let { addAll(it) }
                        // Add new topic messages objects if they don't exist yet
                        newUnreadMessagesByStreamIdAndTopicName[singleStreamUnreadMessages.streamId]
                            ?.filter { it.key !in (newUnreadMessagesByStreamIdAndTopicName[singleStreamUnreadMessages.streamId] ?: emptyMap()) }
                            ?.let { addAll(it.values) }
                    }
                )
            }.let { addAll(it) }
            // Add new object if it didn't exist previously
            newUnreadMessages.asSequence()
                .filter { it.streamId !in oldUnreadMessagesByStreamIdAndTopicName }
                .let { addAll(it) }
        }
        val newData = screenUnfilteredDataRes.map {
            it.setStreamUnreadMessages(newStreamsUnreadMessages)
        }
        return copy(
            streamsUnreadMessages = newStreamsUnreadMessages,
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun List<DelegateItem>.setStreamUnreadMessages(streamUnreadMessages: List<StreamUnreadMessages>): List<DelegateItem> {
        val streamUnreadMessagesById = streamUnreadMessages.associateBy { it.streamId }
        return map { delegateItem ->
            if (delegateItem is Topic) {
                streamUnreadMessagesById[delegateItem.streamId]?.topicsUnreadMessages
                    ?.firstOrNull { topicUnreadMessages -> topicUnreadMessages.topicName == delegateItem.name }
                    ?.let { topicUnreadMessages -> delegateItem.copy(unreadMessagesCount = topicUnreadMessages.unreadMessagesIds.size) }
                    ?: delegateItem
            } else {
                delegateItem
            }
        }
    }
}
