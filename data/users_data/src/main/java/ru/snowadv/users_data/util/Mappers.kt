package ru.snowadv.users_data.util

import ru.snowadv.network.model.AllUsersDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.SingleUserDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.UserDto
import ru.snowadv.users_data.model.DataUser
import ru.snowadv.users_data.model.DataUserStatus

internal fun UserDto.toDataUser(status: DataUserStatus): DataUser {
    return DataUser(id, name, email, avatarUrl, status)
}

internal fun AllUsersDto.toUsersListWithPresences(allUsersPresenceDto: AllUsersPresenceDto): List<DataUser> {
    val userEmailToPresence = allUsersPresenceDto.userEmailToPresenceSources.mapValues {
        DataUserStatus.fromTimestampAndStatus(
            it.value.aggregated.timestamp.toLong(),
            it.value.aggregated.status,
            allUsersPresenceDto.serverTimestamp.toLong()
        )
    }

    return users.map { it.toDataUser(userEmailToPresence[it.email] ?: DataUserStatus.OFFLINE) }
}

internal fun SingleUserDto.toDataUser(
    presenceDto: SingleUserPresenceDto,
    hasOfflineStatus: Boolean
): DataUser {
    return user.toDataUser(
        status = if (hasOfflineStatus) {
            DataUserStatus.fromStatus(presenceDto.presence.aggregated.status)
        } else {
            DataUserStatus.fromTimestampAndStatus(
                presenceDto.presence.aggregated.timestamp.toLong(),
                presenceDto.presence.aggregated.status,
            )
        }
    )
}