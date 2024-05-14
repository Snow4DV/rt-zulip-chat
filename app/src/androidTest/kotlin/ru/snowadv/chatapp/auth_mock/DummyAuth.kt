package ru.snowadv.chatapp.auth_mock

import ru.snowadv.auth_storage.model.StorageAuthUser

object DummyAuth {
    val user = StorageAuthUser(
        id = 1,
        email = "email@example.com",
        apiKey = "123",
    )
}