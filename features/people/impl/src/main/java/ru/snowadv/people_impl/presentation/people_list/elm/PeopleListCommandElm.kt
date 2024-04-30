package ru.snowadv.people_impl.presentation.people_list.elm

import ru.snowadv.event_api.helper.EventQueueProperties

internal sealed interface PeopleListCommandElm {
    data object LoadData : PeopleListCommandElm
    data class ObservePresence(val isRestart: Boolean, val queueProps: EventQueueProperties?) :
        PeopleListCommandElm
    data class OpenProfile(val userId: Long) : PeopleListCommandElm
}
