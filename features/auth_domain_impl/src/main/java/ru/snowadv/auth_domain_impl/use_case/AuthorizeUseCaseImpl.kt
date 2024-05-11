package ru.snowadv.auth_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.auth_domain_api.use_case.AuthorizeUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class AuthorizeUseCaseImpl @Inject constructor(private val authRepo: AuthRepository):
    AuthorizeUseCase {
    override operator fun invoke(username: String, password: String): Flow<Resource<Unit>> {
        return authRepo.auth(username, password)
    }
}