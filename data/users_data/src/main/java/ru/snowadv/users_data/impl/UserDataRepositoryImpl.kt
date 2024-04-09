package ru.snowadv.users_data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.stub.StubZulipApi
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.users_data.model.DataUser
import ru.snowadv.users_data.util.toDataUser
import ru.snowadv.users_data.util.toUsersListWithPresences
import ru.snowadv.utils.asyncAwait
import ru.snowadv.utils.combineFold

class UserDataRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher,
) : UserDataRepository {
    private val api: ZulipApi = StubZulipApi
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
                userDtoResult.combineFold(
                    other = userPresenceDtoResult,
                    onBothSuccess = { userDto, userPresenceDto ->
                        emit(Resource.Success(userDto.toDataUser(userPresenceDto)))
                    },
                    onFailure = {
                        emit(Resource.Error(it))
                    },
                )
            },
        )
    }.flowOn(ioDispatcher)

}