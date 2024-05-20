package ru.snowadv.users_data.repository

import dagger.Reusable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.model.Resource
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.users_data.util.UsersMapper.toPerson
import ru.snowadv.users_data.util.UsersMapper.toUserEntity
import ru.snowadv.users_data.util.UsersMapper.toUsersListWithPresences
import ru.snowadv.users_domain_api.model.Person
import ru.snowadv.users_domain_api.repository.UserRepository
import ru.snowadv.utils.asyncAwait
import ru.snowadv.utils.combineFold
import javax.inject.Inject

@Reusable
internal class UserRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val api: ZulipApi,
    private val authProvider: AuthProvider,
    private val usersDao: UsersDao,
) : UserRepository {
    override fun getPeople(): Flow<Resource<List<Person>>> = flow {
        val cachedData = usersDao.getAllUsers().map { it.toPerson() }.ifEmpty { null }
        emit(Resource.Loading(cachedData))
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
                        val usersWithPresences = usersDto.toUsersListWithPresences(usersPresenceDto)

                        usersDao.updateUsers(usersWithPresences.map { it.toUserEntity() })

                        emit(Resource.Success(usersWithPresences))
                    },
                    onFailure = {
                        emit(Resource.Error(it, cachedData))
                    },
                )
            },
        )
    }.flowOn(dispatcherProvider.io)

    override fun getPerson(id: Long): Flow<Resource<Person>> = flow {
        val cachedData = usersDao.getUser(id)?.toPerson()

        emit(Resource.Loading(cachedData))

        asyncAwait(
            s1 = {
                api.getUser(id).also { userRes -> userRes.getOrNull()?.let { usersDao.insert(it.toUserEntity()) } }
            },
            s2 = {
                api.getUserPresence(id)
            },
            transform = { userDtoResult, userPresenceDtoResult ->
                val resultResource = userDtoResult.getOrNull()?.let {
                    Resource.Success(it.toPerson(userPresenceDtoResult.getOrNull(), true))
                } ?: Resource.Error(userDtoResult.exceptionOrNull() ?: IllegalStateException(), cachedData)

                emit(resultResource)
            },
        )
    }.flowOn(dispatcherProvider.io)

    override fun getCurrentPerson(): Flow<Resource<Person>> {
        return getPerson(authProvider.getAuthorizedUser().id).flowOn(dispatcherProvider.io)
    }
}