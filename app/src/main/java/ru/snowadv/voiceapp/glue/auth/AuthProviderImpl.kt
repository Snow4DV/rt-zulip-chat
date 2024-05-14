package ru.snowadv.voiceapp.glue.auth

import dagger.Reusable
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.data.model.AuthUser
import ru.snowadv.exception.UserNotAuthorizedException
import javax.inject.Inject

@Reusable
class AuthProviderImpl @Inject constructor(
    private val dataRepository: AuthDataRepository,
): AuthProvider {
    override fun getAuthorizedUser(): AuthUser {
        return getAuthorizedUserOrNull()
            ?: throw UserNotAuthorizedException("You are trying to get user authorization before user had authorized. Use getAuthorizedUserOrNull()!")
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