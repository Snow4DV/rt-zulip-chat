package ru.snowadv.profile.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.profile.data.stub.StubData
import ru.snowadv.profile.domain.model.Person
import ru.snowadv.profile.domain.repository.ProfileRepository

internal object StubPeopleRepository: ProfileRepository {

    override fun getPerson(id: Long): Flow<Resource<Person>> = flow{
        emit(Resource.Loading)
        delay(10000)
        StubData.people.firstOrNull { it.id == id }?.let { emit(Resource.Success(it)) } ?: run {
            emit(Resource.Error(IllegalArgumentException("No user with id $id")))
        }
    }
}