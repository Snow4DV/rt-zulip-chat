package ru.snowadv.people_impl.domain.util

import ru.snowadv.users_data_api.model.DataUser
import ru.snowadv.users_data_api.model.DataUserStatus
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
}