package ru.snowadv.auth_data_api

import ru.snowadv.auth_data_api.model.DataAuthUser

interface AuthDataRepository {
    fun getCurrentUser(): DataAuthUser?
}