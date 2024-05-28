package ru.snowadv.users_domain_api.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.users_domain_api.model.Person

interface UserRepository {
    fun getPerson(id: Long): Flow<Resource<Person>>
    fun getPeople(): Flow<Resource<List<Person>>>
    fun getCurrentPerson(): Flow<Resource<Person>>
}