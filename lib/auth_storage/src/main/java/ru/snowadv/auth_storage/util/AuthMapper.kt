package ru.snowadv.auth_storage.util

import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.database.entity.AuthUserEntity
import ru.snowadv.network_authorizer.model.AuthResponseDto

internal object AuthMapper {
    fun AuthUserEntity.toDomainAuthUser(): StorageAuthUser {
        return StorageAuthUser(id = id, email = email, apiKey = apiKey)
    }

    fun StorageAuthUser.toAuthUserEntity(): AuthUserEntity {
        return AuthUserEntity(id = id, email = email, apiKey = apiKey)
    }

    fun AuthResponseDto.toAuthUserEntity(): AuthUserEntity {
        return AuthUserEntity(id = userId, email = email, apiKey = apiKey)
    }

    fun AuthResponseDto.toAuthUser(): StorageAuthUser {
        return StorageAuthUser(id = userId, email = email, apiKey = apiKey)
    }
}