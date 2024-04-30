package ru.snowadv.voiceapp.glue.util

import ru.snowadv.users_data_api.model.DataUser
import ru.snowadv.users_data_api.model.DataUserStatus
import ru.snowadv.profile_api.domain.model.Person as ProfilePerson
import ru.snowadv.people_api.domain.model.Person as PeoplePerson

internal object UsersDataMappers {
    fun DataUser.toPeoplePerson(): PeoplePerson {
        return PeoplePerson(id, fullName, email, avatarUrl, status.toPeoplePersonStatus())
    }

    private fun DataUserStatus.toPeoplePersonStatus(): PeoplePerson.Status {
        return when(this) {
            DataUserStatus.ONLINE -> PeoplePerson.Status.ONLINE
            DataUserStatus.IDLE -> PeoplePerson.Status.IDLE
            DataUserStatus.OFFLINE -> PeoplePerson.Status.OFFLINE
        }
    }

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