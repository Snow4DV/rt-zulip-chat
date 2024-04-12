package ru.snowadv.voiceapp.glue.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.profile.domain.model.Person
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.voiceapp.glue.util.toProfilePerson

class ProfileRepositoryImpl(
    private val userDataRepository: UserDataRepository
): ProfileRepository {
    override fun getPerson(id: Long): Flow<Resource<Person>> {
        return userDataRepository.getUser(id).map { it.map { it.toProfilePerson() } }
    }

    override fun getCurrentPerson(): Flow<Resource<Person>> {
        return userDataRepository.getCurrentUser().map { it.map { it.toProfilePerson() } }
    }
}