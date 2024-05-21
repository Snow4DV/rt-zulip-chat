package ru.snowadv.auth_api.domain.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface AuthorizeUseCase {
    operator fun invoke(username: String, password: String): Flow<Resource<Unit>>
}