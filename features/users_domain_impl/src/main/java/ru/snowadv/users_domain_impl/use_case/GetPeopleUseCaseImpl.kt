package ru.snowadv.users_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.users_domain_api.model.Person
import ru.snowadv.users_domain_api.repository.UserRepository
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import javax.inject.Inject

@Reusable
internal class GetPeopleUseCaseImpl @Inject constructor(
    private val repo: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
): GetPeopleUseCase {
    override operator fun invoke(): Flow<Resource<List<Person>>> {
        return repo.getPeople().map { res ->
            res.map { peopleList ->
                peopleList.sortedWith(compareBy({ it.status.ordinal }, { it.fullName }))
            }
        }.flowOn(dispatcherProvider.default)
    }
}