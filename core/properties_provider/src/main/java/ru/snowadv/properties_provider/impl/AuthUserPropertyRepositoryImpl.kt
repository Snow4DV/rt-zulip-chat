package ru.snowadv.properties_provider.impl

import ru.snowadv.properties_provider.BuildConfig
import ru.snowadv.properties_provider.api.AuthUserPropertyRepository
import ru.snowadv.properties_provider.model.PropertyAuthUser

class AuthUserPropertyRepositoryImpl: AuthUserPropertyRepository {
    override fun getUser(): PropertyAuthUser {

        return PropertyAuthUser(
            id = BuildConfig.USER_ID,
            email = BuildConfig.USER_EMAIL,
            apiKey = BuildConfig.API_KEY,
        )
    }
}