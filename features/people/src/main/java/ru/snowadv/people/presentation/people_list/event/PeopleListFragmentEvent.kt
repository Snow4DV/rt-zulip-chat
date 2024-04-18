package ru.snowadv.people.presentation.people_list.event

sealed class PeopleListFragmentEvent {
    data object FocusOnSearchFieldAndOpenKeyboard: PeopleListFragmentEvent()
}