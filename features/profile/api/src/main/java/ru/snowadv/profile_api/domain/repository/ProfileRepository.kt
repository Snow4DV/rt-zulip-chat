package ru.snowadv.profile_api.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.profile_api.domain.model.Person

interface ProfileRepository {
    fun getPerson(id: Long): Flow<Resource<Person>>
    fun getCurrentPerson(): Flow<Resource<Person>>
}