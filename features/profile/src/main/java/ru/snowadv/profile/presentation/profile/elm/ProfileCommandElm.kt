package ru.snowadv.profile.presentation.profile.elm

import ru.snowadv.event_api.helper.EventQueueProperties

sealed interface ProfileCommandElm { // commands to actor
    data class LoadData(val profileId: Long?) : ProfileCommandElm
    data class ObservePresence(val profileId: Long?, val isRestart: Boolean, val queueProps: EventQueueProperties?) : ProfileCommandElm
    data object GoBack : ProfileCommandElm

}
