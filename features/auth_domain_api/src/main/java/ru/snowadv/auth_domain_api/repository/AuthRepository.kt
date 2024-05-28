package ru.snowadv.auth_domain_api.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.auth_domain_api.model.AuthUser
import ru.snowadv.model.Resource

interface AuthRepository {
    fun getCurrentUser(): AuthUser?
    fun auth(username: String, password: String): Flow<Resource<Unit>>
    suspend fun clearAuth()
}