package ru.snowadv.profile.presentation.profile.state

import ru.snowadv.profile.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState

internal data class ProfileScreenState(
    val screenState: ScreenState<Person> = ScreenState.Loading,
    val isOwner: Boolean
) {
    fun updatePresence(newPresence: Person.Status): ProfileScreenState {
        if (screenState.getCurrentData() == null) return this

        return copy(
            screenState = screenState.map {
                it.copy(
                    status = newPresence
                )
            }
        )
    }
}