package ru.snowadv.home.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.home.domain.model.Person
import ru.snowadv.home.domain.model.Stream

internal interface PeopleRepository {
    fun getPeople(): Flow<Resource<List<Person>>>
    fun getPerson(id: Long): Flow<Resource<Person>>
}