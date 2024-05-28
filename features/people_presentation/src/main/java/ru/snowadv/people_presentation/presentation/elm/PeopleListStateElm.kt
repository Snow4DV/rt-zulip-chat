package ru.snowadv.people_presentation.presentation.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import ru.snowadv.users_domain_api.model.Person

data class PeopleListStateElm(
    val screenState: ScreenState<List<Person>> = ScreenState.Loading(),
    val peopleRes: Resource<List<Person>> = Resource.Loading(),
    val isResumed: Boolean,
    val eventQueueData: EventQueueProperties?,
    val searchQuery: String,
)