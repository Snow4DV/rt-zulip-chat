package ru.snowadv.profile.presentation.profile.elm

import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.profile.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState

data class ProfileStateElm(
    val screenState: ScreenState<Person> = ScreenState.Loading,
    val profileId: Long?,
    val eventQueueData: EventQueueProperties?,
) {
    val isOwner = profileId == null
}