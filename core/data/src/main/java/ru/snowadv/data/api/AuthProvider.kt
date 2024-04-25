package ru.snowadv.data.api

import ru.snowadv.data.model.AuthUser

interface AuthProvider {
    /**
     * This function should only be called in screens in authorized part of the app.
     * That means information about authorized user should already be obtained.
     * In case the session dies, BadAuthBehavior will kick in (TODO, will be implemented later)
     */
    fun getAuthorizedUser(): AuthUser

    /**
     * This function can be called in any part of the app but case with unauthorized user should
     * be implemented
     */
    fun getAuthorizedUserOrNull(): AuthUser?
}