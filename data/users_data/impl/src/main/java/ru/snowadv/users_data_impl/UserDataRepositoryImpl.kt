package ru.snowadv.users_data_impl

import dagger.Reusable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.users_data_api.UserDataRepository
import ru.snowadv.users_data_api.model.DataUser
import ru.snowadv.users_data_impl.util.UsersMapper.toDataUser
import ru.snowadv.users_data_impl.util.UsersMapper.toUsersListWithPresences
import ru.snowadv.utils.asyncAwait
import ru.snowadv.utils.combineFold
import javax.inject.Inject

@Reusable
class UserDataRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
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
                        emit(Resource.Error(it, null))
                    },
                )
            },
        )
    }.flowOn(dispatcherProvider.io)

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
                } ?: Resource.Error(userDtoResult.exceptionOrNull() ?: IllegalStateException())

                emit(resultResource)
            },
        )
    }.flowOn(dispatcherProvider.io)

    override fun getCurrentUser(): Flow<Resource<DataUser>> {
        return getUser(authProvider.getAuthorizedUser().id)
    }

}