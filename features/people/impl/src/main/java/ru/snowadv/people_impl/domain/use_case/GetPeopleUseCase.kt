package ru.snowadv.people_impl.domain.use_case

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.people_impl.domain.model.Person
import ru.snowadv.people_impl.domain.repository.PeopleRepository
import javax.inject.Inject

@Reusable
internal class GetPeopleUseCase @Inject constructor(
    private val repo: PeopleRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<Resource<List<Person>>> {
        return repo.getPeople().map { res ->
            res.map { peopleList ->
                peopleList.sortedWith(compareBy({ it.status.ordinal }, { it.fullName }))
            }
        }.flowOn(dispatcherProvider.default)
    }
}