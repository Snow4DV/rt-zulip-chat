package ru.snowadv.channels.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.presentation.channel_list.view_model.ChannelListViewModel

internal class StreamListViewModelFactory(
    private val streamType: StreamType,
    private val router: ChannelsRouter,
    private val searchQueryFlow: Flow<String>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreamListViewModel::class.java)) {
            return StreamListViewModel(
                type = streamType,
                router = router,
                searchQueryFlow = searchQueryFlow
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}