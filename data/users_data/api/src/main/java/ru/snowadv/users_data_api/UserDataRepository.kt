package ru.snowadv.users_data_api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource
import ru.snowadv.users_data_api.model.DataUser

interface UserDataRepository {
    fun getAllUsers(): Flow<Resource<List<DataUser>>>
    fun getUser(userId: Long): Flow<Resource<DataUser>>
    fun getCurrentUser(): Flow<Resource<DataUser>>
}