package ru.snowadv.voiceapp.glue.util

import ru.snowadv.profile_api.domain.model.Person as ProfilePerson
import ru.snowadv.people_api.domain.model.Person as PeoplePerson
import ru.snowadv.users_data.model.DataUser
import ru.snowadv.users_data.model.DataUserStatus

object UsersDataMappers {
    internal fun DataUser.toPeoplePerson(): PeoplePerson {
        return PeoplePerson(id, fullName, email, avatarUrl, status.toPeoplePersonStatus())
    }

    internal fun DataUserStatus.toPeoplePersonStatus(): PeoplePerson.Status {
        return when(this) {
            DataUserStatus.ONLINE -> PeoplePerson.Status.ONLINE
            DataUserStatus.IDLE -> PeoplePerson.Status.IDLE
            DataUserStatus.OFFLINE -> PeoplePerson.Status.OFFLINE
        }
    }

    internal fun DataUser.toProfilePerson(): ProfilePerson {
        return ProfilePerson(id, fullName, email, avatarUrl, status.toProfilePersonStatus())
    }

    internal fun DataUserStatus.toProfilePersonStatus(): ProfilePerson.Status {
        return when(this) {
            DataUserStatus.ONLINE -> ProfilePerson.Status.ONLINE
            DataUserStatus.IDLE -> ProfilePerson.Status.IDLE
            DataUserStatus.OFFLINE -> ProfilePerson.Status.OFFLINE
        }
    }
}