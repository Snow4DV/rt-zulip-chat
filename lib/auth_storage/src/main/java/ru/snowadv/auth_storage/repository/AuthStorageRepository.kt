package ru.snowadv.auth_storage.repository

import kotlinx.coroutines.flow.Flow
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.model.Resource

interface AuthStorageRepository {
    suspend fun loadAuthFromDatabase(): StorageAuthUser?
    fun getCurrentUser(): StorageAuthUser?
    fun auth(username: String, password: String): Flow<Resource<Unit>>
    suspend fun clearAuth()
}