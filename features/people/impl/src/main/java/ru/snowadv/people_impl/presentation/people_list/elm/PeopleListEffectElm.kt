package ru.snowadv.people_impl.presentation.people_list.elm

sealed interface PeopleListEffectElm {
    data object FocusOnSearchFieldAndOpenKeyboard : PeopleListEffectElm
}

