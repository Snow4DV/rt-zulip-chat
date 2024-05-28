package ru.snowadv.auth_data.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.auth_data.util.AuthMapper.toDomainUser
import ru.snowadv.auth_domain_api.model.AuthUser
import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.model.Resource
import ru.snowadv.module_injector.general.PerScreen
import javax.inject.Inject
@PerScreen
internal class AuthRepositoryImpl @Inject constructor(
    private val authStorageRepository: AuthStorageRepository,
): AuthRepository {

    override fun getCurrentUser(): AuthUser? {
        return authStorageRepository.getCurrentUser()?.toDomainUser()
    }

    override fun auth(username: String, password: String): Flow<Resource<Unit>> {
        return authStorageRepository.auth(username, password)
    }
    override suspend fun clearAuth() {
        authStorageRepository.clearAuth()
    }
}
