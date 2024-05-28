package ru.snowadv.auth_storage.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.auth_storage.util.AuthMapper.toAuthUser
import ru.snowadv.auth_storage.util.AuthMapper.toAuthUserEntity
import ru.snowadv.auth_storage.util.AuthMapper.toDomainAuthUser
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.model.Resource
import ru.snowadv.model.toUnitResource
import ru.snowadv.network_authorizer.api.ZulipAuthApi
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
internal class AuthStorageRepositoryImpl @Inject constructor(
    private val authDao: AuthDao,
    private val authApi: ZulipAuthApi,
): AuthStorageRepository {
    private var user: StorageAuthUser? = null

    override fun getCurrentUser(): StorageAuthUser? {
        return user
    }

    override suspend fun loadAuthFromDatabase(): StorageAuthUser? {
        user = authDao.getCurrentAuth()?.toDomainAuthUser()
        return user
    }

    override fun auth(username: String, password: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        authApi.authorize(username, password).fold(
            onSuccess = { it ->
                Resource.Success(it.toAuthUser())
            },
            onFailure = {
                Resource.Error(it)
            },
        ).let { res ->
            res.getDataOrNull()?.let {
                    goodUser -> authDao.replaceAuthUser(goodUser.toAuthUserEntity())
                user = goodUser
            }
            emit(res)
        }
    }.map { it.toUnitResource() }

    override suspend fun clearAuth() {
        authDao.clearAuth()
        user = null
    }
}
