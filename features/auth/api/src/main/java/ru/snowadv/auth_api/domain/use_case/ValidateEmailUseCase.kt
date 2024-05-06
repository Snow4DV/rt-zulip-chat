package ru.snowadv.auth_api.domain.use_case

interface ValidateEmailUseCase {
    operator fun invoke(email: String): Boolean
}