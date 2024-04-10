package ru.snowadv.people.presentation.people_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.people.domain.navigation.PeopleRouter

internal class PeopleListViewModelFactory(
    private val router: PeopleRouter
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeopleListViewModel::class.java)) {
            return PeopleListViewModel(router) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}