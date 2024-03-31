package ru.snowadv.home.presentation.people_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.home.presentation.channel_list.view_model.ChannelListSharedViewModel
import ru.snowadv.home.presentation.navigation.HomeRouter

internal class PeopleListViewModelFactory(
    private val router: HomeRouter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeopleListViewModel::class.java)) {
            return PeopleListViewModel(router) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}