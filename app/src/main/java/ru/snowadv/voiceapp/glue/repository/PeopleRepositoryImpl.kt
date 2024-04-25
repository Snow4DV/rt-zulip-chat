package ru.snowadv.voiceapp.glue.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.people.domain.model.Person
import ru.snowadv.people.domain.repository.PeopleRepository
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.voiceapp.glue.util.toPeoplePerson

class PeopleRepositoryImpl(
    private val userDataRepository: UserDataRepository,
    private val defaultDispatcher: CoroutineDispatcher,
): PeopleRepository {
    override fun getPeople(): Flow<Resource<List<Person>>> {
        return userDataRepository.getAllUsers().map { res ->
            res.map { dataUsers ->
                dataUsers.map { dataUser -> dataUser.toPeoplePerson() }
            }
        }.flowOn(defaultDispatcher)
    }
}