package ru.snowadv.auth_domain_api.use_case

interface ValidatePasswordUseCase {
    operator fun invoke(password: String): Boolean
}