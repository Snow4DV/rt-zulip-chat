package ru.snowadv.users_data_impl.util

import ru.snowadv.database.entity.UserEntity
import ru.snowadv.network.model.AllUsersResponseDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.SingleUserResponseDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.UserResponseDto
import ru.snowadv.users_data_api.model.DataUser
import ru.snowadv.users_data_api.model.DataUserStatus

internal object UsersMapper {
    fun UserResponseDto.toDataUser(status: DataUserStatus): DataUser {
        return DataUser(id, name, email, avatarUrl, status)
    }

    fun AllUsersResponseDto.toUsersListWithPresences(allUsersPresenceDto: AllUsersPresenceDto): List<DataUser> {
        val userEmailToPresence = allUsersPresenceDto.userEmailToPresenceSources.mapValues {
            DataUserStatus.fromTimestampAndStatus(
                it.value.aggregated.timestamp.toLong(),
                it.value.aggregated.status,
                allUsersPresenceDto.serverTimestamp.toLong()
            )
        }

        return users.map { it.toDataUser(userEmailToPresence[it.email] ?: DataUserStatus.OFFLINE) }
    }

    fun SingleUserResponseDto.toDataUser(
        presenceDto: SingleUserPresenceDto? = null,
        hasOfflineStatus: Boolean
    ): DataUser {
        return user.toDataUser(
            status = if (presenceDto == null) {
                DataUserStatus.UNKNOWN
            } else if (hasOfflineStatus) {
                DataUserStatus.fromStatus(presenceDto.presence.aggregated.status)
            } else {
                DataUserStatus.fromTimestampAndStatus(
                    presenceDto.presence.aggregated.timestamp.toLong(),
                    presenceDto.presence.aggregated.status,
                )
            }
        )
    }

    fun UserEntity.toDataUser(): DataUser {
        return DataUser(
            id = id,
            fullName = fullName,
            email = email,
            avatarUrl = avatarUrl,
            status = DataUserStatus.UNKNOWN,
        )
    }

    fun DataUser.toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            fullName = fullName,
            email = email,
            avatarUrl = avatarUrl,
        )
    }

    fun SingleUserResponseDto.toUserEntity(): UserEntity {
        return UserEntity(
            id = this.user.id,
            fullName = this.user.name,
            email = this.user.email,
            avatarUrl = this.user.avatarUrl,
        )
    }
}