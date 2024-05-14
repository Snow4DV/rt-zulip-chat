package ru.snowadv.profile_impl.data.util

import ru.snowadv.users_data_api.model.DataUser
import ru.snowadv.users_data_api.model.DataUserStatus
import ru.snowadv.profile_impl.domain.model.Person as ProfilePerson

internal object UsersDataMappers {

    fun DataUser.toProfilePerson(): ProfilePerson {
        return ProfilePerson(id, fullName, email, avatarUrl, status.toProfilePersonStatus())
    }

    private fun DataUserStatus.toProfilePersonStatus(): ProfilePerson.Status {
        return when(this) {
            DataUserStatus.ONLINE -> ProfilePerson.Status.ONLINE
            DataUserStatus.IDLE -> ProfilePerson.Status.IDLE
            DataUserStatus.OFFLINE -> ProfilePerson.Status.OFFLINE
        }
    }
}