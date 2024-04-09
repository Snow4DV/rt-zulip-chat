package ru.snowadv.profile.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.profile.domain.model.Person

interface ProfileRepository {
    fun getPerson(id: Long): Flow<Resource<Person>>
}