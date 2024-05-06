package ru.snowadv.auth_data_impl

import dagger.Reusable
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.auth_data_api.model.DataAuthUser
import ru.snowadv.exception.UserNotAuthorizedException
import javax.inject.Inject

@Reusable
internal class AuthProviderImpl @Inject constructor(
    private val dataRepository: AuthDataRepository,
): AuthProvider {
    override fun getAuthorizedUser(): DataAuthUser {
        return getAuthorizedUserOrNull()
            ?: throw UserNotAuthorizedException("You are trying to get user authorization before user had authorized. Use getAuthorizedUserOrNull()!")
    }

    override fun getAuthorizedUserOrNull(): DataAuthUser? {
        return dataRepository.getCurrentUser()
    }
}