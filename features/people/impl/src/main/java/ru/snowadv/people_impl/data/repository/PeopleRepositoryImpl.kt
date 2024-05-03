package ru.snowadv.people_impl.data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.people_impl.domain.model.Person
import ru.snowadv.people_impl.domain.repository.PeopleRepository
import ru.snowadv.people_impl.domain.util.UsersDataMappers.toPeoplePerson
import ru.snowadv.users_data_api.UserDataRepository
import javax.inject.Inject

@Reusable
class PeopleRepositoryImpl @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val dispatcherProvider: DispatcherProvider,
): PeopleRepository {
    override fun getPeople(): Flow<Resource<List<Person>>> {
        return userDataRepository.getAllUsers().map { res ->
            res.map { dataUsers ->
                dataUsers.map { dataUser -> dataUser.toPeoplePerson() }
            }
        }.flowOn(dispatcherProvider.default)
    }
}