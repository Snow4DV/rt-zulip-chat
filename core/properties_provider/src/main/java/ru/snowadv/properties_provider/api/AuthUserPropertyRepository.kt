package ru.snowadv.properties_provider.api

import ru.snowadv.properties_provider.model.PropertyAuthUser

/**
 * This module is introduced as a stub until the authorization flow is finished.
 */
interface AuthUserPropertyRepository {
    fun getUser(): PropertyAuthUser
}