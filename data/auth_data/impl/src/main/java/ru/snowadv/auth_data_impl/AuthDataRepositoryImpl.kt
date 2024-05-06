package ru.snowadv.auth_data_impl

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.model.DataAuthUser
import ru.snowadv.auth_data_impl.util.AuthMapper.toAuthUserEntity
import ru.snowadv.auth_data_impl.util.AuthMapper.toDataAuthUser
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.model.Resource
import ru.snowadv.network_authorizer.api.ZulipAuthApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * That's pretty basic implementation but it will become bigger and include all methods that will
 * be required on the authorization screen. TODO
 *
 * This repository will be state-full. It will hold current user object in memory after authorization
 * or "session-is-alive" check during startup so users will be able to obtain current user without
 * suspending and quickly.
 */
@Singleton
internal class AuthDataRepositoryImpl @Inject constructor(
    private val authDao: AuthDao,
    private val authApi: ZulipAuthApi,
): AuthDataRepository {
    private var user: DataAuthUser? = null

    override fun getCurrentUser(): DataAuthUser? {
        return user
    }

    override suspend fun loadAuthFromDatabase(): DataAuthUser? {
        user = authDao.getCurrentAuth()?.toDataAuthUser()
        return user
    }

    override fun auth(username: String, password: String): Flow<Resource<DataAuthUser>> = flow {
        emit(Resource.Loading())
        authApi.authorize(username, password).fold(
            onSuccess = {
                Resource.Success(it.toDataAuthUser())
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
    }

    override suspend fun clearAuth() {
        authDao.clearAuth()
        user = null
    }
}
