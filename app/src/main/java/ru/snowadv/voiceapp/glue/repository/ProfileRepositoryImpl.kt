package ru.snowadv.voiceapp.glue.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.profile.domain.model.Person
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.voiceapp.glue.util.toProfilePerson

class ProfileRepositoryImpl(
    private val userDataRepository: UserDataRepository,
    private val defaultDispatcher: CoroutineDispatcher,
): ProfileRepository {
    override fun getPerson(id: Long): Flow<Resource<Person>> {
        return userDataRepository.getUser(id).map { res ->
            res.map { dataUser -> dataUser.toProfilePerson() }
        }.flowOn(defaultDispatcher)
    }

    override fun getCurrentPerson(): Flow<Resource<Person>> {
        return userDataRepository.getCurrentUser().map { res ->
            res.map { dataUser -> dataUser.toProfilePerson() }
        }.flowOn(defaultDispatcher)
    }
}