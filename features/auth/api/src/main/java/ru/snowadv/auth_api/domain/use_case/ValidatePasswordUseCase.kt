package ru.snowadv.auth_api.domain.use_case

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): Boolean
}