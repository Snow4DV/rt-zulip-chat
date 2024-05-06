package ru.snowadv.auth_data_impl.util

import ru.snowadv.auth_data_api.model.DataAuthUser
import ru.snowadv.database.entity.AuthUserEntity
import ru.snowadv.network_authorizer.model.AuthResponseDto

internal object AuthMapper {
    fun AuthUserEntity.toDataAuthUser(): DataAuthUser {
        return DataAuthUser(id = id, email = email, apiKey = apiKey)
    }

    fun DataAuthUser.toAuthUserEntity(): AuthUserEntity {
        return AuthUserEntity(id = id, email = email, apiKey = apiKey)
    }

    fun ru.snowadv.network_authorizer.model.AuthResponseDto.toAuthUserEntity(): AuthUserEntity {
        return AuthUserEntity(id = userId, email = email, apiKey = apiKey)
    }

    fun ru.snowadv.network_authorizer.model.AuthResponseDto.toDataAuthUser(): DataAuthUser {
        return DataAuthUser(id = userId, email = email, apiKey = apiKey)
    }
}