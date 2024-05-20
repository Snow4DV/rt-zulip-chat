package ru.snowadv.users_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.users_domain_api.model.Person
import ru.snowadv.users_domain_api.repository.UserRepository
import ru.snowadv.users_domain_api.use_case.GetProfileUseCase
import javax.inject.Inject

@Reusable
internal class GetProfileUseCaseImpl @Inject constructor(
    private val repo: UserRepository
): GetProfileUseCase {

    /**
     * @param userId Id of user whose profile should be shown.
     * Will show current user's profile if not passed
     */
    override operator fun invoke(userId: Long?): Flow<Resource<Person>> {
        return userId?.let { id -> repo.getPerson(id) } ?: repo.getCurrentPerson()
    }
}