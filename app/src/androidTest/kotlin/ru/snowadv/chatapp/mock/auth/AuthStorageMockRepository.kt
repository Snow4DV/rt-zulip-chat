package ru.snowadv.chatapp.mock.auth

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.chatapp.data.MockData
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class AuthStorageMockRepository @Inject constructor(private val data: MockData): AuthStorageRepository {
    override suspend fun loadAuthFromDatabase(): StorageAuthUser? {
        return data.user
    }

    override fun getCurrentUser(): StorageAuthUser? {
        return data.user
    }

    override fun auth(username: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Success(Unit))
    }

    override suspend fun clearAuth() {
        throw IllegalStateException("Cant log out in authorized-only tests")
    }
}