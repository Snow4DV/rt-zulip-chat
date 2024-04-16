package ru.snowadv.people.presentation.people_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.people.domain.use_case.GetPeopleUseCase
import ru.snowadv.people.domain.use_case.ListenToPresenceEventsUseCase

internal class PeopleListViewModelFactory(
    private val router: PeopleRouter,
    private val getPeopleUseCase: GetPeopleUseCase,
    private val listenToPresenceEventsUseCase: ListenToPresenceEventsUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeopleListViewModel::class.java)) {
            return PeopleListViewModel(
                router = router,
                getPeopleUseCase = getPeopleUseCase,
                listenToPresenceEventsUseCase = listenToPresenceEventsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}