package ru.snowadv.auth_data.impl

import ru.snowadv.auth_data.api.AuthDataRepository
import ru.snowadv.auth_data.model.DataAuthUser
import ru.snowadv.properties_provider.api.AuthUserPropertyRepository

/**
 * That's pretty basic implementation but it will become bigger and include all methods that will
 * be required on the authorization screen. TODO
 *
 * This repository will be state-ful. It will hold current user object in memory after authorization
 * or "session-is-alive" check during startup so users will be able to obtain current user without
 * suspending and quickly.
 */
class AuthDataRepositoryImpl(
    private val authProviderPropertyRepository: AuthUserPropertyRepository,
): AuthDataRepository {
    override fun getCurrentUser(): DataAuthUser {
        return with(authProviderPropertyRepository.getUser()) {
            DataAuthUser(
                id = id,
                email = email,
                apiKey = apiKey,
            )
        }
    }


}