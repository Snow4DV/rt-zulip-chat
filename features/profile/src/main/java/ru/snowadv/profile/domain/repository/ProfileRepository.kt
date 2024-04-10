package ru.snowadv.profile.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.profile.domain.model.Person

internal interface ProfileRepository {
    fun getPerson(id: Long): Flow<Resource<Person>>
}