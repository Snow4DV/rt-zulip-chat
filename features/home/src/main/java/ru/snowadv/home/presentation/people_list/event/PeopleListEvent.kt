package ru.snowadv.home.presentation.people_list.event

sealed class PeopleListEvent {
    class ChangedSearchQuery(val newQuery: String): PeopleListEvent()
    class ClickedOnPerson(val userId: Long): PeopleListEvent()
    data object ClickedOnRetry: PeopleListEvent()
}