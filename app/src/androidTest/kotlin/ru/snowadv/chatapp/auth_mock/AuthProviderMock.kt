package ru.snowadv.chatapp.auth_mock

import ru.snowadv.auth_storage.model.StorageAuthUser
import ru.snowadv.auth_storage.provider.AuthProvider

internal object AuthProviderMock: AuthProvider {
    override fun getAuthorizedUser(): StorageAuthUser = DummyAuth.user

    override fun getAuthorizedUserOrNull(): StorageAuthUser = DummyAuth.user

    override fun getAuthorizedUserId(): Long = DummyAuth.user.id

    override fun getAuthorizedUserIdOrNull(): Long = DummyAuth.user.id
}