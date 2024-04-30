package ru.snowadv.voiceapp.glue.repository

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.people_api.domain.model.Person
import ru.snowadv.people_api.domain.repository.PeopleRepository
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.voiceapp.glue.util.UsersDataMappers.toPeoplePerson
import javax.inject.Inject

@Reusable
class PeopleRepositoryImpl @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val dispatcherProvider: DispatcherProvider,
): ru.snowadv.people_api.domain.repository.PeopleRepository {
    override fun getPeople(): Flow<Resource<List<ru.snowadv.people_api.domain.model.Person>>> {
        return userDataRepository.getAllUsers().map { res ->
            res.map { dataUsers ->
                dataUsers.map { dataUser -> dataUser.toPeoplePerson() }
            }
        }.flowOn(dispatcherProvider.default)
    }
}