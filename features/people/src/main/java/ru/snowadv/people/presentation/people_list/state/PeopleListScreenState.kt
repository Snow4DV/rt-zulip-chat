package ru.snowadv.people.presentation.people_list.state

import ru.snowadv.people.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState

internal data class PeopleListScreenState(
    val searchQuery: String = "",
    val screenState: ScreenState<List<Person>> = ScreenState.Loading,
) {

    fun filteredScreenState(): ScreenState<List<Person>> {
        return screenState.getCurrentData()
            ?.filter { it.fullName.contains(other = searchQuery, ignoreCase = true) }
            ?.let { ScreenState.Success(it) } ?: screenState
    }
}