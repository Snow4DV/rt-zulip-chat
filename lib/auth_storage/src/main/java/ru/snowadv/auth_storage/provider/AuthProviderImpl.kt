package ru.snowadv.auth_storage.provider

import dagger.Reusable
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.exception.UserNotAuthorizedException
import javax.inject.Inject

@Reusable
internal class AuthProviderImpl @Inject constructor(
    private val authStorageRepository: AuthStorageRepository,
): AuthProvider {
    override fun getAuthorizedUser(): StorageAuthUser {
        return getAuthorizedUserOrNull()
            ?: throw UserNotAuthorizedException("You are trying to get user authorization before user had authorized. Use getAuthorizedUserOrNull()!")
    }

    override fun getAuthorizedUserOrNull(): StorageAuthUser? {
        return authStorageRepository.getCurrentUser()
    }

    override fun getAuthorizedUserId(): Long {
        return getAuthorizedUser().id
    }

    override fun getAuthorizedUserIdOrNull(): Long? {
        return getAuthorizedUserOrNull()?.id
    }
}