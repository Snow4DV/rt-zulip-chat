package ru.snowadv.auth_domain_api.use_case

interface ValidateEmailUseCase {
    operator fun invoke(email: String): Boolean
}