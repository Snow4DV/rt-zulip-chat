package ru.snowadv.chatapp.auth_mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.model.Resource

internal object AuthStorageMockRepository: AuthStorageRepository {
    override suspend fun loadAuthFromDatabase(): StorageAuthUser? {
        return DummyAuth.user
    }

    override fun getCurrentUser(): StorageAuthUser? {
        return DummyAuth.user
    }

    override fun auth(username: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Success(Unit))
    }

    override suspend fun clearAuth() {
        throw IllegalStateException("Cant log out in authorized-only tests")
    }
}