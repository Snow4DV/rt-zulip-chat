package ru.snowadv.users_data.util

import ru.snowadv.database.entity.UserEntity
import ru.snowadv.network.model.AllUsersResponseDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.SingleUserResponseDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.UserResponseDto
import ru.snowadv.users_domain_api.model.Person
import java.time.Instant

internal object UsersMapper {
    // This variable describes how long can user be inactive to still be considered online/idle
    private const val ONLINE_DELTA_SEC = 60 * 5

    fun UserResponseDto.toPerson(status: Person.Status): Person {
        return Person(id, name, email, avatarUrl, status)
    }

    fun AllUsersResponseDto.toUsersListWithPresences(allUsersPresenceDto: AllUsersPresenceDto): List<Person> {
        val userEmailToPresence = allUsersPresenceDto.userEmailToPresenceSources.mapValues {
            personStatusFromTimestampAndApiStatus(
                it.value.aggregated.timestamp.toLong(),
                it.value.aggregated.status,
                allUsersPresenceDto.serverTimestamp.toLong()
            )
        }

        return users.map { it.toPerson(userEmailToPresence[it.email] ?: Person.Status.OFFLINE) }
    }

    fun SingleUserResponseDto.toPerson(
        presenceDto: SingleUserPresenceDto? = null,
        hasOfflineStatus: Boolean
    ): Person {
        return user.toPerson(
            status = if (presenceDto == null) {
                Person.Status.UNKNOWN
            } else if (hasOfflineStatus) {
                Person.Status.entries.firstOrNull { it.apiName == presenceDto.presence.aggregated.status } ?: Person.Status.UNKNOWN
            } else {
                personStatusFromTimestampAndApiStatus(
                    presenceDto.presence.aggregated.timestamp.toLong(),
                    presenceDto.presence.aggregated.status,
                )
            }
        )
    }

    fun UserEntity.toPerson(): Person {
        return Person(
            id = id,
            fullName = fullName,
            email = email,
            avatarUrl = avatarUrl,
            status = Person.Status.UNKNOWN,
        )
    }

    fun Person.toUserEntity(): UserEntity {
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

    private fun personStatusFromTimestampAndApiStatus(
        timestamp: Long,
        apiStatus: String,
        serverTimestamp: Long = Instant.now().epochSecond, // Server timestamp is not always available
    ): Person.Status {
        return when {
            (serverTimestamp - timestamp) > ONLINE_DELTA_SEC -> Person.Status.OFFLINE
            apiStatus == Person.Status.ONLINE.apiName -> Person.Status.ONLINE
            apiStatus == Person.Status.IDLE.apiName -> Person.Status.IDLE
            else -> Person.Status.OFFLINE
        }
    }
}