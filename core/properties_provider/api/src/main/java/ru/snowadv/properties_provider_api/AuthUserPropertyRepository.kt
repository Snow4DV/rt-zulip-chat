package ru.snowadv.properties_provider_api

import ru.snowadv.properties_provider_api.model.PropertyAuthUser

/**
 * This module is introduced as a stub until the authorization flow is finished.
 */
interface AuthUserPropertyRepository {
    fun getUser(): PropertyAuthUser
}