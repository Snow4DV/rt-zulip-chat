package ru.snowadv.users_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.users_domain_api.model.Person
interface GetProfileUseCase {
    operator fun invoke(userId: Long? = null): Flow<Resource<Person>>
}