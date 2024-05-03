package ru.snowadv.profile_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.profile_impl.domain.model.Person
import ru.snowadv.profile_impl.domain.repository.ProfileRepository
import javax.inject.Inject

@Reusable
internal class GetProfileUseCase @Inject constructor(
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