package ru.snowadv.profile.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.profile.domain.model.Person
import ru.snowadv.profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repo: ProfileRepository
) {

    /**
     * @param userId Id of user whose profile should be shown.
     * Will show current user's profile if not passed
     */
    operator fun invoke(userId: Long? = null): Flow<Resource<Person>> {
        return userId?.let { id -> repo.getPerson(id) } ?: repo.getCurrentPerson()
    }
}