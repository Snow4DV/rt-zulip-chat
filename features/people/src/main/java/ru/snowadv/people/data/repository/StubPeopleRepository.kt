package ru.snowadv.people.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.domain.model.Resource
import ru.snowadv.people.data.stub.StubData
import ru.snowadv.people.domain.model.Person
import ru.snowadv.people.domain.repository.PeopleRepository

internal object StubPeopleRepository: PeopleRepository {
    override fun getPeople(): Flow<Resource<List<Person>>> = flow {
        emit(Resource.Loading)
        delay(200)
        emit(Resource.Success(StubData.people))
    }
}