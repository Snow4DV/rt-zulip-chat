package ru.snowadv.auth_data.api

import kotlinx.coroutines.flow.Flow
import ru.snowadv.auth_data.model.DataAuthUser

interface AuthDataRepository {
    fun getCurrentUser(): DataAuthUser?
}