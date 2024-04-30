package ru.snowadv.voiceapp.glue.repository

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.profile_api.domain.model.Person
import ru.snowadv.profile_api.domain.repository.ProfileRepository
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.voiceapp.glue.util.UsersDataMappers.toProfilePerson
import javax.inject.Inject

@Reusable
class ProfileRepositoryImpl @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val dispatcherProvider: DispatcherProvider,
): ru.snowadv.profile_api.domain.repository.ProfileRepository {
    override fun getPerson(id: Long): Flow<Resource<ru.snowadv.profile_api.domain.model.Person>> {
        return userDataRepository.getUser(id).map { res ->
            res.map { dataUser -> dataUser.toProfilePerson() }
        }.flowOn(dispatcherProvider.default)
    }

    override fun getCurrentPerson(): Flow<Resource<ru.snowadv.profile_api.domain.model.Person>> {
        return userDataRepository.getCurrentUser().map { res ->
            res.map { dataUser -> dataUser.toProfilePerson() }
        }.flowOn(dispatcherProvider.default)
    }
}