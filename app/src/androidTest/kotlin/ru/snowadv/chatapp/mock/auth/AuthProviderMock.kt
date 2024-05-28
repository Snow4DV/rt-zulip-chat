package ru.snowadv.chatapp.mock.auth

import dagger.Reusable
import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.chatapp.data.MockData
import javax.inject.Inject

@Reusable
internal class AuthProviderMock @Inject constructor(private val data: MockData): AuthProvider {
    override fun getAuthorizedUser(): StorageAuthUser = data.user

    override fun getAuthorizedUserOrNull(): StorageAuthUser = data.user

    override fun getAuthorizedUserId(): Long = data.user.id

    override fun getAuthorizedUserIdOrNull(): Long = data.user.id
}