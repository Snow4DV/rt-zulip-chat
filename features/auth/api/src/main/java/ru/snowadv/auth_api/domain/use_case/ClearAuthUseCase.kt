package ru.snowadv.auth_api.domain.use_case

interface ClearAuthUseCase {
    suspend operator fun invoke()
}