package ru.snowadv.profile_impl.data.repository

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.profile_impl.domain.model.Person
import ru.snowadv.profile_impl.domain.repository.ProfileRepository
import ru.snowadv.profile_impl.data.util.UsersDataMappers.toProfilePerson
import ru.snowadv.users_data_api.UserDataRepository
import javax.inject.Inject

@Reusable
class ProfileRepositoryImpl @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val dispatcherProvider: DispatcherProvider,
): ProfileRepository {
    override fun getPerson(id: Long): Flow<Resource<Person>> {
        return userDataRepository.getUser(id).map { res ->
            res.map { dataUser -> dataUser.toProfilePerson() }
        }.flowOn(dispatcherProvider.default)
    }

    override fun getCurrentPerson(): Flow<Resource<Person>> {
        return userDataRepository.getCurrentUser().map { res ->
            res.map { dataUser -> dataUser.toProfilePerson() }
        }.flowOn(dispatcherProvider.default)
    }
}