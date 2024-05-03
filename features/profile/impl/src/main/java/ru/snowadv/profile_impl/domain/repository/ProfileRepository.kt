package ru.snowadv.profile_impl.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.profile_impl.domain.model.Person

interface ProfileRepository {
    fun getPerson(id: Long): Flow<Resource<Person>>
    fun getCurrentPerson(): Flow<Resource<Person>>
}