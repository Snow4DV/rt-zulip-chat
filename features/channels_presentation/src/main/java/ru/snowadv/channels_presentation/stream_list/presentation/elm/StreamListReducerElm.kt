package ru.snowadv.channels_presentation.stream_list.presentation.elm

import ru.snowadv.channels_domain_api.model.Stream
import ru.snowadv.channels_domain_api.model.StreamTopics
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.channels_domain_api.model.StreamUnreadMessages
import ru.snowadv.channels_domain_api.model.Topic
import ru.snowadv.channels_domain_api.model.TopicUnreadMessages
import ru.snowadv.events_api.helper.StateMachineQueueHelper
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import ru.snowadv.model.map
import ru.snowadv.presentation.util.filterList
import ru.snowadv.presentation.util.toScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class StreamListReducerElm @Inject constructor() :
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
                    if (!event.isRead) {
                        addNewMessage(
                            event.streamId,
                            event.topicName,
                            event.messageId,
                            event.eventId
                        )
                    } else {
                        copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                    }.addTopic(
                        streamId = event.streamId,
                        topicName = event.topicName,
                        isNewMessageRead = event.isRead,
                    )
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
                    }.subscribedToStreams(event.changedStreams.map { it.id }.toSet())
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.ServerEvent.UserSubscriptionsRemovedEvent -> {
                state {
                    if (state.streamType == StreamType.SUBSCRIBED) {
                        removeStreams(event.changedStreams, event.eventId)
                    } else {
                        copy(eventQueueData = eventQueueData?.copy(lastEventId = event.eventId))
                    }.unsubscribedFromStreams(event.changedStreams.map { it.id }.toSet())
                }
                commandObserve()
            }

            is StreamListEventElm.Internal.StreamsLoaded -> {
                state {
                    val newData =
                        if (event.cached) Resource.Success(event.streams) else Resource.Success(
                            event.streams
                        )
                    copy(
                        screenUnfilteredDataRes = newData,
                        screenState = newData.toScreenState().filterStreamsByQuery(state.searchQuery),
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
                showTopicsLoading(event.streamId)
            }

            is StreamListEventElm.Internal.TopicsLoadingErrorWithCachedTopics -> state {
                loadTopics(event.streamId, event.cachedTopics)
            }


            is StreamListEventElm.Internal.TopicsLoadingError -> {
                state {
                    hideTopics()
                }
                effects {
                    +StreamListEffectElm.ShowInternetErrorWithRetry(
                        StreamListEventElm.Ui.ClickedOnExpandStream(
                            event.streamId,
                        )
                    )
                }
            }

            is StreamListEventElm.Internal.SubscribingToStream -> state {
                changeSubscribingToStream(streamId = event.streamId, subscribing = true)
            }

            is StreamListEventElm.Internal.ErrorWhileSubscribingToStream -> state {
                changeSubscribingToStream(streamId = event.streamId, subscribing = false)
            }

            is StreamListEventElm.Internal.ChangedSubscriptionToStream -> state {
                if (event.isSubscribed) {
                    subscribedToStreams(setOf(event.streamId))
                } else {
                    unsubscribedFromStreams(setOf(event.streamId))
                }
            }
        }
    }

    override fun Result.ui(event: StreamListEventElm.Ui) {
        when (event) {
            is StreamListEventElm.Ui.ClickedOnTopic -> {
                state.selectedStream?.let { stream ->
                    commands {
                        +StreamListCommandElm.GoToTopic(stream.id, stream.name, event.topicName)
                    }
                }
            }

            StreamListEventElm.Ui.Init, StreamListEventElm.Ui.ReloadClicked -> commands {
                +StreamListCommandElm.LoadStreams(state.streamType)
            }

            is StreamListEventElm.Ui.ClickedOnExpandStream -> {
                if (state.selectedStream?.id == event.streamId) {
                    state { hideTopics() }
                } else {
                    commands {
                        +StreamListCommandElm.LoadTopics(event.streamId)
                    }
                }
            }

            StreamListEventElm.Ui.Paused -> {
                state {
                    copy(resumed = false)
                }
                commands {
                    +StreamListCommandElm.StopObservation
                }
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
                        screenState = screenUnfilteredDataRes.toScreenState()
                            .filterStreamsByQuery(event.query),
                    )
                }
            }

            is StreamListEventElm.Ui.ClickedOnChangeStreamSubscriptionStatus -> commands {
                if (!event.stream.subscribing) {
                    +StreamListCommandElm.ChangeSubscriptionStatusForStream(event.stream.id, event.stream.name, !event.stream.subscribed)
                }
            }

            is StreamListEventElm.Ui.ClickedOnOpenStream -> commands {
                +StreamListCommandElm.GoToStream(event.streamId, event.streamName)
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

    private fun StreamListStateElm.addStreams(
        streams: List<Stream>,
        eventId: Long
    ): StreamListStateElm {
        val streamIds = screenState.getCurrentData()?.map { it.id }?.toSet() ?: emptySet()
        val newData =
            screenUnfilteredDataRes.map { streamsList ->
                (streamsList + streams.filter { stream -> stream.id !in streamIds })
                    .sortedBy { it.name }
                    .setStreamUnreadMessagesForStreams(streamsUnreadMessages)
            }
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
            screenState = screenState.filterList { it.id !in removedStreamIds },
            screenUnfilteredDataRes = screenUnfilteredDataRes.map { list ->
                list.filter { it.id !in removedStreamIds }
            },
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.changeSubscribingToStream(streamId: Long, subscribing: Boolean): StreamListStateElm {
        return copy(
            screenState = screenState.map { streams ->
                streams.map { stream ->
                    if (stream.id == streamId) {
                        stream.copy(subscribing = subscribing)
                    } else {
                        stream
                    }
                }
            }
        )
    }

    private fun StreamListStateElm.subscribedToStreams(streamIds: Set<Long>): StreamListStateElm {
        return copy(
            screenState = screenState.map { streams ->
                streams.map { stream ->
                    if (stream.id in streamIds) {
                        stream.copy(subscribing = false, subscribed = true)
                    } else {
                        stream
                    }
                }
            }
        )
    }

    private fun StreamListStateElm.unsubscribedFromStreams(streamIds: Set<Long>): StreamListStateElm {
        return copy(
            screenState = screenState.map { streams ->
                streams.map { stream ->
                    if (stream.id in streamIds) {
                        stream.copy(subscribing = false, subscribed = false)
                    } else {
                        stream
                    }
                }
            }
        )
    }

    private fun StreamListStateElm.loadTopics(
        streamId: Long,
        topics: List<Topic>
    ): StreamListStateElm {
        return copy(
            topics = StreamTopics(
                streamId = streamId,
                topics = Resource.Success(topics.setStreamUnreadMessagesForTopics(streamsUnreadMessages)),
            ),
        )
    }

    private fun StreamListStateElm.showTopicsLoading(streamId: Long): StreamListStateElm {
        return copy(
            topics = StreamTopics(
                streamId = streamId,
                topics = Resource.Loading(),
            ),
        )
    }


    private fun StreamListStateElm.updateStream(
        streamId: Long,
        streamName: String,
        eventId: Long,
    ): StreamListStateElm {
        val newData = screenUnfilteredDataRes.map {
            it.map { stream ->
                if (stream.id == streamId) {
                    stream.copy(
                        name = streamName
                    )
                } else {
                    stream
                }
            }.setStreamUnreadMessagesForStreams(streamsUnreadMessages)
        }
        return copy(
            screenState = newData.toScreenState().filterStreamsByQuery(searchQuery),
            screenUnfilteredDataRes = newData,
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.hideTopics(): StreamListStateElm {
        return copy(
            topics = null,
        )
    }

    private fun ScreenState<List<Stream>>.filterStreamsByQuery(searchQuery: String): ScreenState<List<Stream>> {
        return filterList {
            it.name.contains(
                other = searchQuery,
                ignoreCase = true,
            )
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
            screenState = screenState.map { it.setStreamUnreadMessagesForStreams(streamsUnreadMessages) },
            screenUnfilteredDataRes = screenUnfilteredDataRes.map { it.setStreamUnreadMessagesForStreams(streamsUnreadMessages) },
            topics = topics?.copy(topics = topics.topics.map {
                it.setStreamUnreadMessagesForTopics(
                    streamsUnreadMessages
                )
            }),
            eventQueueData = EventQueueProperties(
                queueId = queueId,
                timeoutSeconds = timeoutSeconds,
                lastEventId = eventId
            ),
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
                StreamUnreadMessages(
                    streamId,
                    listOf(TopicUnreadMessages(topicName, listOf(messageId)))
                )
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
        return copy(
            streamsUnreadMessages = newStreamsUnreadMessages,
            screenState = screenState.map { it.setStreamUnreadMessagesForStreams(newStreamsUnreadMessages) },
            screenUnfilteredDataRes = screenUnfilteredDataRes.map { it.setStreamUnreadMessagesForStreams(newStreamsUnreadMessages) },
            topics = topics?.copy(topics = topics.topics.map {
                it.setStreamUnreadMessagesForTopics(
                    streamsUnreadMessages
                )
            }),
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
                            ?.filter {
                                it.key !in (newUnreadMessagesByStreamIdAndTopicName[singleStreamUnreadMessages.streamId]
                                    ?: emptyMap())
                            }
                            ?.let { addAll(it.values) }
                    }
                )
            }.let { addAll(it) }
            // Add new object if it didn't exist previously
            newUnreadMessages.asSequence()
                .filter { it.streamId !in oldUnreadMessagesByStreamIdAndTopicName }
                .let { addAll(it) }
        }
        return copy(
            streamsUnreadMessages = newStreamsUnreadMessages,
            screenState = screenState.map { it.setStreamUnreadMessagesForStreams(newStreamsUnreadMessages) },
            screenUnfilteredDataRes = screenUnfilteredDataRes.map { it.setStreamUnreadMessagesForStreams(newStreamsUnreadMessages) },
            topics = topics?.copy(topics = topics.topics.map {
                it.setStreamUnreadMessagesForTopics(
                    newStreamsUnreadMessages
                )
            }),
            eventQueueData = eventQueueData?.copy(lastEventId = eventId),
        )
    }

    private fun StreamListStateElm.addTopic(
        streamId: Long,
        topicName: String,
        isNewMessageRead: Boolean
    ): StreamListStateElm {
        return if (selectedStream?.id == streamId) {
            copy(
                topics = topics?.copy(topics = topics.topics.map {
                    it.addNewTopicToExistingTopics(
                        streamId = streamId,
                        newTopicName = topicName,
                        isNewMessageRead = isNewMessageRead,
                    )
                })
            )
        } else {
            this
        }
    }

    private fun List<Topic>.setStreamUnreadMessagesForTopics(streamUnreadMessages: List<StreamUnreadMessages>): List<Topic> {
        val streamUnreadMessagesById = streamUnreadMessages.associateBy { it.streamId }
        return map { topic ->
            streamUnreadMessagesById[topic.streamId]?.topicsUnreadMessages
                ?.firstOrNull { topicUnreadMessages -> topicUnreadMessages.topicName == topic.name }
                ?.let { topicUnreadMessages -> topic.copy(unreadMessagesCount = topicUnreadMessages.unreadMessagesIds.size) }
                ?: topic
        }
    }

    private fun List<Stream>.setStreamUnreadMessagesForStreams(streamUnreadMessages: List<StreamUnreadMessages>): List<Stream> {
        val streamUnreadMessagesCountById = streamUnreadMessages.associateBy { it.streamId }
            .mapValues { it.value.topicsUnreadMessages.map { unreadMessagesIds ->
                unreadMessagesIds.unreadMessagesIds.size }.sum()
            }
        return map { stream ->
            stream.copy(unreadMessagesCount = streamUnreadMessagesCountById[stream.id] ?: 0)
        }
    }

    private fun List<Topic>.addNewTopicToExistingTopics(
        streamId: Long,
        newTopicName: String,
        isNewMessageRead: Boolean
    ): List<Topic> {
        return if (this.none { it.name == newTopicName }) {
            (this + createTopicByNameWithUnreadMessage(
                streamId,
                newTopicName,
                isNewMessageRead
            )).sortedBy { it.name }
        } else {
            this
        }
    }

    private fun createTopicByNameWithUnreadMessage(
        streamId: Long,
        topicName: String,
        isNewMessageRead: Boolean
    ): Topic {
        return Topic(
            uniqueId = "${streamId}_${topicName}",
            name = topicName,
            streamId = streamId,
            unreadMessagesCount = if (isNewMessageRead) 0 else 1,
        )
    }
}
