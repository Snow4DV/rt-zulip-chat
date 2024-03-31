package ru.snowadv.home.presentation.profile.state

import ru.snowadv.home.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState

internal data class ProfileScreenState(
    val screenState: ScreenState<Person> =  ScreenState.Loading,
    val isOwner: Boolean
)