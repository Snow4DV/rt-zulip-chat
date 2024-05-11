package ru.snowadv.people_impl.presentation.people_list.elm

import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.people_impl.presentation.model.Person
import ru.snowadv.model.ScreenState

internal data class PeopleListStateElm(
    val screenState: ScreenState<List<Person>> = ScreenState.Loading(),
    val peopleRes: Resource<List<Person>> = Resource.Loading(),
    val isResumed: Boolean,
    val eventQueueData: EventQueueProperties?,
    val searchQuery: String,
)