package ru.snowadv.channels.presentation.stream_list.state

import ru.snowadv.channels.presentation.model.Stream
import ru.snowadv.channels.presentation.model.StreamIdContainer
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.model.ScreenState.Loading.filtered

internal data class StreamListScreenState(
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading, // Source of truth
) {
    val selectedStream: Stream? by lazy {
        (screenState as? ScreenState.Success<List<DelegateItem>>)
            ?.data?.asSequence()
            ?.filterIsInstance<Stream>()
            ?.firstOrNull { it.expanded }
    }


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
                )
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
        if (searchQuery.isBlank()) return this // Filter empty search queries
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
            screenState = screenState
                .filtered {
                    it is Stream && it.id in filteredStreamIds
                            || it is StreamIdContainer && it.streamId in filteredStreamIds
                }
        )
    }
}