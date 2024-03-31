package ru.snowadv.home.presentation.channel_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.home.presentation.navigation.HomeRouter
import ru.snowadv.home.presentation.stream_list.view_model.StreamListViewModel

internal class ChannelListViewModelFactory(
    private val router: HomeRouter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelListSharedViewModel::class.java)) {
            return ChannelListSharedViewModel(router) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}