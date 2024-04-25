package ru.snowadv.users_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.users_data.model.DataUser
import ru.snowadv.users_data.util.UsersMapper.toDataUser
import ru.snowadv.users_data.util.UsersMapper.toUsersListWithPresences
import ru.snowadv.utils.asyncAwait
import ru.snowadv.utils.combineFold

class UserDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
    private val api: ZulipApi,
    private val authProvider: AuthProvider,
) : UserDataRepository {
    override fun getAllUsers(): Flow<Resource<List<DataUser>>> = flow {
        asyncAwait(
            s1 = {
                api.getAllUsers()
            },
            s2 = {
                api.getAllUsersPresence()
            },
            transform = { usersDtoResult, usersPresencesDtoResult ->
                usersDtoResult.combineFold(
                    other = usersPresencesDtoResult,
                    onBothSuccess = { usersDto, usersPresenceDto ->
                        emit(Resource.Success(usersDto.toUsersListWithPresences(usersPresenceDto)))
                    },
                    onFailure = {
                        emit(Resource.Error(it))
                    },
                )
            },
        )
    }.flowOn(ioDispatcher)

    override fun getUser(userId: Long): Flow<Resource<DataUser>> = flow {
        asyncAwait(
            s1 = {
                api.getUser(userId)
            },
            s2 = {
                api.getUserPresence(userId)
            },
            transform = { userDtoResult, userPresenceDtoResult ->
                val resultResource = userDtoResult.getOrNull()?.let {
                    Resource.Success(it.toDataUser(userPresenceDtoResult.getOrNull(), true))
                } ?: Resource.Error(userDtoResult.exceptionOrNull())

                emit(resultResource)
            },
        )
    }.flowOn(ioDispatcher)

    override fun getCurrentUser(): Flow<Resource<DataUser>> {
        return getUser(authProvider.getAuthorizedUser().id)
    }

}