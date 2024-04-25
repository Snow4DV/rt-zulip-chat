package ru.snowadv.people.presentation.people_list.state

import ru.snowadv.people.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState

internal data class PeopleListScreenState(
    val screenState: ScreenState<List<Person>> = ScreenState.Loading,
) {

    fun filterBySearchQuery(searchQuery: String): PeopleListScreenState {
        return copy(
            screenState = screenState.getCurrentData()
                ?.filter { it.fullName.contains(other = searchQuery, ignoreCase = true) }
                ?.let { ScreenState.Success(it) } ?: screenState,
        )
    }

    fun updateStatus(userId: Long, status: Person.Status): PeopleListScreenState { // TODO think of solution to get offline events too
        return copy(
            screenState = screenState.map { people ->
                people.map { person ->
                    if (person.id == userId) {
                        person.copy(status = status)
                    } else {
                        person
                    }
                }.sortedWith(compareBy({ it.status.ordinal }, { it.fullName }))
            }
        )
    }
}