package ru.snowadv.home.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.home.domain.model.StreamType
import ru.snowadv.home.presentation.channel_list.view_model.ChannelListSharedViewModel

internal class StreamListViewModelFactory(
    private val streamType: StreamType,
    private val listSharedViewModel: ChannelListSharedViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreamListViewModel::class.java)) {
            return StreamListViewModel(streamType, listSharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}