package ru.snowadv.auth_data_api

import ru.snowadv.auth_data_api.model.DataAuthUser


interface AuthProvider {
    /**
     * This function should only be called in screens in authorized part of the app.
     * That means information about authorized user should already be obtained.
     * In case the session dies, BadAuthBehavior will kick in (TODO, will be implemented later)
     */
    fun getAuthorizedUser(): DataAuthUser

    /**
     * This function can be called in any part of the app but case with unauthorized user should
     * be implemented
     */
    fun getAuthorizedUserOrNull(): DataAuthUser?
}