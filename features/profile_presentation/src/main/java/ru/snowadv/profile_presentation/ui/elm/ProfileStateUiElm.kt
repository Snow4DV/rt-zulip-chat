package ru.snowadv.profile_presentation.ui.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.profile_presentation.ui.model.UiPerson
import ru.snowadv.model.ScreenState
import ru.snowadv.users_domain_api.model.Person

internal data class ProfileStateUiElm(
    val screenState: ScreenState<UiPerson> = ScreenState.Loading(),
    val profileId: Long?,
    val eventQueueData: EventQueueProperties?,
    val isResumed: Boolean,
) {
    val isOwner = profileId == null
}