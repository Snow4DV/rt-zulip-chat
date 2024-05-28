package ru.snowadv.auth_domain_impl.use_case

import dagger.Reusable
import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.auth_domain_api.use_case.ClearAuthUseCase
import javax.inject.Inject

@Reusable
internal class ClearAuthUseCaseImpl @Inject constructor(private val authRepository: AuthRepository):
    ClearAuthUseCase {
    override suspend fun invoke() {
        authRepository.clearAuth()
    }
}