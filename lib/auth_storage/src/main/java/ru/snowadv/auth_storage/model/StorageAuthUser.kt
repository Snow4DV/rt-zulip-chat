package ru.snowadv.auth_storage.model

data class StorageAuthUser(
    val id: Long,
    val email: String,
    val apiKey: String,
)