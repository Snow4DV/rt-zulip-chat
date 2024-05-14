package ru.snowadv.people.presentation.people_list.elm

import ru.snowadv.event_api.helper.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.people.presentation.model.Person
import ru.snowadv.presentation.model.ScreenState

internal data class PeopleListStateElm(
    val screenState: ScreenState<List<Person>> = ScreenState.Loading,
    val peopleRes: Resource<List<Person>> = Resource.Loading,
    val isResumed: Boolean,
    val eventQueueData: EventQueueProperties?,
    val searchQuery: String,
)