package ru.snowadv.voiceapp.glue.auth

import ru.snowadv.auth_data.api.AuthDataRepository
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.data.model.AuthUser

class AuthProviderImpl(
    private val dataRepository: AuthDataRepository,
): AuthProvider {
    override fun getAuthorizedUser(): AuthUser {
        return getAuthorizedUserOrNull()
            ?: error("You are trying to get user authorization before user had authorized. Use getAuthorizedUserOrNull()!!")
    }

    override fun getAuthorizedUserOrNull(): AuthUser? {
        return dataRepository.getCurrentUser()?.let {
            AuthUser(
                id = it.id,
                email = it.email,
                apiKey = it.apiKey
            )
        }
    }
}