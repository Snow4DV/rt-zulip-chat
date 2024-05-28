package ru.snowadv.auth_domain_api.use_case

interface ClearAuthUseCase {
    suspend operator fun invoke()
}