package ru.snowadv.people_api.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.people_api.domain.model.Person

interface PeopleRepository {
    fun getPeople(): Flow<Resource<List<Person>>>
}