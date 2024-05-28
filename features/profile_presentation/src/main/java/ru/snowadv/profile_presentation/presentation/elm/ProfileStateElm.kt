package ru.snowadv.profile_presentation.presentation.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.profile_presentation.ui.model.UiPerson
import ru.snowadv.model.ScreenState
import ru.snowadv.users_domain_api.model.Person

data class ProfileStateElm(
    val screenState: ScreenState<Person> = ScreenState.Loading(),
    val profileId: Long?,
    val eventQueueData: EventQueueProperties?,
    val isResumed: Boolean,
) {
    val isOwner = profileId == null
}