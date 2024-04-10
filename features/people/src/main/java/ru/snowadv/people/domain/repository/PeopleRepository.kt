package ru.snowadv.people.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.people.domain.model.Person

internal interface PeopleRepository {
    fun getPeople(): Flow<Resource<List<Person>>>
}