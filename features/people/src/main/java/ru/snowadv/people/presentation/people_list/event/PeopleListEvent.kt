package ru.snowadv.people.presentation.people_list.event

sealed class PeopleListEvent {
    class ClickedOnPerson(val userId: Long): PeopleListEvent()
    data object ClickedOnRetry: PeopleListEvent()
    data object ClickedOnSearchIcon: PeopleListEvent()
}