package ru.snowadv.auth_impl.data

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_api.domain.repository.AuthRepository
import ru.snowadv.model.Resource
import ru.snowadv.model.toUnitResource
import javax.inject.Inject

@Reusable
internal class AuthRepositoryImpl @Inject constructor(private val authDataRepo: AuthDataRepository):
    AuthRepository {
    override fun auth(username: String, password: String): Flow<Resource<Unit>> {
        return authDataRepo.auth(username = username, password = password)
            .map { it.toUnitResource() }
    }

    override suspend fun clearAuth() {
        authDataRepo.clearAuth()
    }
}