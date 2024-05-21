package ru.snowadv.auth_api.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface AuthRepository {
    fun auth(username: String, password: String): Flow<Resource<Unit>>
    suspend fun clearAuth()
}