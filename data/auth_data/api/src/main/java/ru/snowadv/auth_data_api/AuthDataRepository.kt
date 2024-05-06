package ru.snowadv.auth_data_api

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import ru.snowadv.auth_data_api.model.DataAuthUser
import ru.snowadv.model.Resource

interface AuthDataRepository {
    fun getCurrentUser(): DataAuthUser?
    fun auth(username: String, password: String): Flow<Resource<DataAuthUser>>
    suspend fun loadAuthFromDatabase(): DataAuthUser?
    suspend fun clearAuth()
}