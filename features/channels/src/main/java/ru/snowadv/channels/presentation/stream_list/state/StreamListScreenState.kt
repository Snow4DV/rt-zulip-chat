package ru.snowadv.channels.presentation.stream_list.state

import ru.snowadv.channels.presentation.model.Stream
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class StreamListScreenState(
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading, // Source of truth
    val searchQuery: String = "",
    val actionInProgress: Boolean = false,
) {
    val isSearching: Boolean = searchQuery.isNotBlank()
    val selectedStream: Stream? =
        (screenState as? ScreenState.Success<List<DelegateItem>>)
            ?.data?.asSequence()
            ?.filterIsInstance<Stream>()
            ?.firstOrNull { it.expanded }

    /**
     * That function returns current screen state that is filtered by search query if it is present
     */
    fun filteredScreenState(): ScreenState<List<DelegateItem>> {
        return if (screenState is ScreenState.Success && isSearching) {
            val filteredIds = screenState.data
                .asSequence()
                .mapNotNull {
                    if (it is Stream && it.name.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    ) {
                        it.id
                    } else {
                        null
                    }
                }
                .toSet()
            val resultData = screenState.data
                .filter { it is Stream && it.id in filteredIds || it is Topic && it.streamId in filteredIds }
            if (resultData.isNotEmpty()) ScreenState.Success(resultData) else ScreenState.Empty
        } else {
            screenState
        }
    }

    fun loadTopics(streamId: Long, topics: List<Topic>): StreamListScreenState {
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
}