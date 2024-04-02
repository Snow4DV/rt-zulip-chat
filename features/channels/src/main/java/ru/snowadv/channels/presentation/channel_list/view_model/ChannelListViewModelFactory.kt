package ru.snowadv.channels.presentation.channel_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.channels.domain.navigation.ChannelsRouter
internal class ChannelListViewModelFactory(
    private val router: ChannelsRouter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChannelListSharedViewModel::class.java)) {
            return ChannelListSharedViewModel(router) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}