package ru.snowadv.people_presentation.ui.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import ru.snowadv.people_presentation.ui.model.UiPerson
import ru.snowadv.users_domain_api.model.Person

data class PeopleListStateUiElm(
    val screenState: ScreenState<List<UiPerson>> = ScreenState.Loading(),
    val peopleRes: Resource<List<UiPerson>> = Resource.Loading(),
    val isResumed: Boolean,
    val eventQueueData: EventQueueProperties?,
    val searchQuery: String,
)