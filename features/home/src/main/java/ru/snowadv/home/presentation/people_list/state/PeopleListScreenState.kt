package ru.snowadv.home.presentation.people_list.state

import ru.snowadv.home.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState
import ru.snowadv.presentation.model.ScreenState.Loading.filtered

internal data class PeopleListScreenState(
    val searchQuery: String = "",
    val screenState: ScreenState<List<Person>> = ScreenState.Loading,
) {

    fun filteredScreenState(): ScreenState<List<Person>> {
        return screenState.filtered { it.fullName.contains(other = searchQuery, ignoreCase = true) }
    }
}