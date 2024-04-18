package ru.snowadv.users_data.util

import ru.snowadv.network.model.AllUsersResponseDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.SingleUserResponseDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.UserResponseDto
import ru.snowadv.users_data.model.DataUser
import ru.snowadv.users_data.model.DataUserStatus

internal fun UserResponseDto.toDataUser(status: DataUserStatus): DataUser {
    return DataUser(id, name, email, avatarUrl, status)
}

internal fun AllUsersResponseDto.toUsersListWithPresences(allUsersPresenceDto: AllUsersPresenceDto): List<DataUser> {
    val userEmailToPresence = allUsersPresenceDto.userEmailToPresenceSources.mapValues {
        DataUserStatus.fromTimestampAndStatus(
            it.value.aggregated.timestamp.toLong(),
            it.value.aggregated.status,
            allUsersPresenceDto.serverTimestamp.toLong()
        )
    }

    return users.map { it.toDataUser(userEmailToPresence[it.email] ?: DataUserStatus.OFFLINE) }
}

internal fun SingleUserResponseDto.toDataUser(
    presenceDto: SingleUserPresenceDto? = null,
    hasOfflineStatus: Boolean
): DataUser {
    return user.toDataUser(
        status = if (presenceDto == null) {
            DataUserStatus.OFFLINE
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