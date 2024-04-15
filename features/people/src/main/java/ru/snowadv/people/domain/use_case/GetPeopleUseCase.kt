package ru.snowadv.people.domain.use_case

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.snowadv.model.Resource
import ru.snowadv.model.map
import ru.snowadv.people.domain.model.Person
import ru.snowadv.people.domain.repository.PeopleRepository

internal class GetPeopleUseCase(
    private val repo: PeopleRepository,
) {
    operator fun invoke(): Flow<Resource<List<Person>>> {
        return repo.getPeople().map { res ->
            res.map { peopleList ->
                peopleList.sortedWith(compareBy({ it.status.ordinal }, { it.fullName }))
            }
        }.flowOn(Dispatchers.Default)
    }
}