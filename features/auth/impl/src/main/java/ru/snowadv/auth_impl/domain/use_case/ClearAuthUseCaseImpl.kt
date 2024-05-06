package ru.snowadv.auth_impl.domain.use_case

import dagger.Reusable
import ru.snowadv.auth_api.domain.repository.AuthRepository
import ru.snowadv.auth_api.domain.use_case.ClearAuthUseCase
import javax.inject.Inject

@Reusable
internal class ClearAuthUseCaseImpl @Inject constructor(private val authRepository: AuthRepository): ClearAuthUseCase {
    override suspend fun invoke() {
        authRepository.clearAuth()
    }
}