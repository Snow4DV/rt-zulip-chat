package ru.snowadv.people_presentation.presentation.elm

import ru.snowadv.events_api.model.EventQueueProperties

internal sealed interface PeopleListCommandElm {
    data object LoadData : PeopleListCommandElm
    data class ObservePresence(val isRestart: Boolean, val queueProps: EventQueueProperties?) :
        PeopleListCommandElm
    data class OpenProfile(val userId: Long) : PeopleListCommandElm
}
