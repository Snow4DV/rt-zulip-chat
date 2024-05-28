package ru.snowadv.people_presentation.presentation.elm

sealed interface PeopleListEffectElm {
    data object FocusOnSearchFieldAndOpenKeyboard : PeopleListEffectElm
}

