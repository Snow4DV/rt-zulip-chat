package ru.snowadv.profile_presentation.presentation.elm

import ru.snowadv.events_api.model.EventQueueProperties

internal sealed interface ProfileCommandElm { // commands to actor
    data class LoadData(val profileId: Long?) : ProfileCommandElm
    data class ObservePresence(val profileId: Long?, val isRestart: Boolean, val queueProps: EventQueueProperties?) :
        ProfileCommandElm
    data object GoBack : ProfileCommandElm
    data object Logout : ProfileCommandElm
    data object StopObservation : ProfileCommandElm
}
