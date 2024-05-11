package ru.snowadv.profile_impl.presentation.profile.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.profile_impl.presentation.model.Person
import ru.snowadv.model.ScreenState

internal data class ProfileStateElm(
    val screenState: ScreenState<Person> = ScreenState.Loading(),
    val profileId: Long?,
    val eventQueueData: EventQueueProperties?,
    val isResumed: Boolean,
) {
    val isOwner = profileId == null
}