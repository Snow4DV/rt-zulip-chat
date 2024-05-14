package ru.snowadv.properties_provider_impl

import ru.snowadv.properties_provider_api.AuthUserPropertyRepository
import ru.snowadv.properties_provider_api.model.PropertyAuthUser
import javax.inject.Inject


internal class AuthUserPropertyRepositoryImpl @Inject constructor(): AuthUserPropertyRepository {
    override fun getUser(): PropertyAuthUser {

        return PropertyAuthUser(
            id = BuildConfig.USER_ID,
            email = BuildConfig.USER_EMAIL,
            apiKey = BuildConfig.API_KEY,
        )
    }
}