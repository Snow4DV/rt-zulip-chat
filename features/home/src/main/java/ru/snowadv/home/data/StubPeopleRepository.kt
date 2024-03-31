package ru.snowadv.home.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.home.domain.model.Person
import ru.snowadv.home.domain.repository.PeopleRepository

internal object StubPeopleRepository: PeopleRepository {
    override fun getPeople(): Flow<Resource<List<Person>>> = flow {
        emit(Resource.Loading)
        delay(200)
        emit(Resource.Success(StubData.people))
    }

    override fun getPerson(id: Long): Flow<Resource<Person>> = flow{
        emit(Resource.Loading)
        delay(500)
        StubData.people.firstOrNull { it.id == id }?.let { emit(Resource.Success(it)) } ?: run {
            emit(Resource.Error(IllegalArgumentException("No user with id $id")))
        }
    }
}