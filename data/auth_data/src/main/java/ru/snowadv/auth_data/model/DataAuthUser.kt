package ru.snowadv.auth_data.model

data class DataAuthUser(
    val id: Long,
    val email: String,
    val apiKey: String,
)