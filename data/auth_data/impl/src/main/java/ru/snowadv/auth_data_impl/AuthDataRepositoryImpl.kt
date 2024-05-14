package ru.snowadv.auth_data_impl

import dagger.Reusable
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.model.DataAuthUser
import ru.snowadv.properties_provider_api.AuthUserPropertyRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * That's pretty basic implementation but it will become bigger and include all methods that will
 * be required on the authorization screen. TODO
 *
 * This repository will be state-full. It will hold current user object in memory after authorization
 * or "session-is-alive" check during startup so users will be able to obtain current user without
 * suspending and quickly.
 */
@Singleton
internal class AuthDataRepositoryImpl @Inject constructor(
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