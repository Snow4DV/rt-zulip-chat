package ru.snowadv.auth_storage.provider

import ru.snowadv.auth_storage.model.StorageAuthUser


interface AuthProvider {
    /**
     * This function should only be called in screens in authorized part of the app.
     * That means information about authorized user should already be obtained.
     * In case the session dies, BadAuthBehavior will kick in
     */
    fun getAuthorizedUser(): StorageAuthUser

    /**
     * This function can be called in any part of the app but case with unauthorized user should
     * be implemented
     */
    fun getAuthorizedUserOrNull(): StorageAuthUser?

    fun getAuthorizedUserId(): Long
    fun getAuthorizedUserIdOrNull(): Long?
}