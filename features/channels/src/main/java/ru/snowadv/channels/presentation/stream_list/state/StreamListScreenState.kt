package ru.snowadv.channels.presentation.stream_list.state

import ru.snowadv.channels.presentation.model.Stream
import ru.snowadv.channels.presentation.model.StreamIdContainer
import ru.snowadv.channels.presentation.model.StreamUnreadMessages
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.channels.presentation.model.TopicUnreadMessages
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class StreamListScreenState(
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading, // Source of truth
    val streamsUnreadMessages: List<StreamUnreadMessages> = emptyList(),
) {
    val selectedStream: Stream? = (screenState as? ScreenState.Success<List<DelegateItem>>)
        ?.data?.asSequence()
        ?.filterIsInstance<Stream>()
        ?.firstOrNull { it.expanded }


    fun loadTopics(streamId: Long, topics: List<DelegateItem>): StreamListScreenState {
        return if (screenState is ScreenState.Success) {
            copy(
                screenState = ScreenState.Success(
                    buildList {
                        screenState.data.filterIsInstance<Stream>().forEach { item ->
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
                    }
                ).setStreamUnreadMessages(streamsUnreadMessages)
            )
        } else {
            this
        }
    }

    fun hideTopics(): StreamListScreenState {
        return if (screenState is ScreenState.Success) {
            copy(
                screenState = ScreenState.Success(
                    screenState.data.mapNotNull {
                        if (it is Stream && it.expanded) {
                            it.copy(expanded = false)
                        } else if (it is Stream) {
                            it
                        } else {
                            null
                        }
                    }
                )
            )
        } else {
            this
        }
    }

    fun filterStreamsByQuery(searchQuery: String): StreamListScreenState {
        val filteredStreamIds = screenState.getCurrentData()
            ?.asSequence()
            ?.mapNotNull {
                if (it is Stream && it.name.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                ) it.id else null
            }
            ?.toSet() ?: emptySet()
        return copy(
            screenState = screenState.getCurrentData()
                ?.filter {
                    it is Stream && it.id in filteredStreamIds
                            || it is StreamIdContainer && it.streamId in filteredStreamIds
                }
                ?.let { ScreenState.Success(it) } ?: screenState
        )
    }

    fun setInitialUnreadMessages(streamsUnreadMessages: List<StreamUnreadMessages>): StreamListScreenState {
        return copy(
            streamsUnreadMessages = streamsUnreadMessages,
            screenState = screenState.setStreamUnreadMessages(streamsUnreadMessages)
        )
    }

    fun markMessagesAsRead(messagesIdsList: List<Long>): StreamListScreenState {
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
            screenState = screenState.setStreamUnreadMessages(newStreamsUnreadMessages),
        )
    }

    fun addNewMessage(streamId: Long, topicName: String, messageId: Long): StreamListScreenState {
        return markMessagesAsUnread(
            listOf(
                StreamUnreadMessages(
                    streamId,
                    listOf(TopicUnreadMessages(topicName, listOf(messageId)))
                )
            )
        )
    }

    fun markMessagesAsUnread(nowUnreadMessages: List<StreamUnreadMessages>): StreamListScreenState {
        val nowUnreadMessagesByStreamIdAndTopicName = nowUnreadMessages.groupBy { it.streamId }
            .mapValues {
                it.value.flatMap { streamsUnreadMessages -> streamsUnreadMessages.topicsUnreadMessages }
                    .associateBy { topicUnreadMessages -> topicUnreadMessages.topicName }
            }
        val newStreamsUnreadMessages = streamsUnreadMessages.map { streamUnreadMessages ->
            streamUnreadMessages.copy(
                topicsUnreadMessages = streamUnreadMessages.topicsUnreadMessages.map { topicUnreadMessages ->
                    topicUnreadMessages.copy(
                        unreadMessagesIds = buildSet {
                            addAll(topicUnreadMessages.unreadMessagesIds)
                            nowUnreadMessagesByStreamIdAndTopicName[streamUnreadMessages.streamId]
                                ?.get(topicUnreadMessages.topicName)
                                ?.let { prevTopicUnreadMessages ->
                                    addAll(prevTopicUnreadMessages.unreadMessagesIds.toSet())
                                }
                        }.toList()
                    )
                }
            )
        }
        return copy(
            streamsUnreadMessages = newStreamsUnreadMessages,
            screenState = screenState.setStreamUnreadMessages(newStreamsUnreadMessages),
        )
    }

    private fun ScreenState<List<DelegateItem>>.setStreamUnreadMessages(streamUnreadMessages: List<StreamUnreadMessages>): ScreenState<List<DelegateItem>> {
        val streamUnreadMessagesById = streamUnreadMessages.associateBy { it.streamId }
        return map { list ->
            list.map { delegateItem ->
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
}