package ru.snowadv.auth_data.util

import ru.snowadv.auth_domain_api.model.AuthUser
import ru.snowadv.auth_storage.model.StorageAuthUser

internal object AuthMapper {
    fun StorageAuthUser.toDomainUser(): AuthUser {
        return AuthUser(id = id, email = email, apiKey = apiKey)
    }
}