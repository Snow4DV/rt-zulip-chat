package ru.snowadv.people_presentation.ui.elm

sealed interface PeopleListEventUiElm {
    data object Init : PeopleListEventUiElm
    data object Paused : PeopleListEventUiElm
    data object Resumed : PeopleListEventUiElm
    data object ClickedOnRetry : PeopleListEventUiElm
    data object ClickedOnSearchIcon : PeopleListEventUiElm
    data class ClickedOnPerson(val userId: Long) : PeopleListEventUiElm
    data class ChangedSearchQuery(val query: String) : PeopleListEventUiElm
}