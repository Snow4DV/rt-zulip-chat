package ru.snowadv.auth_data_api.model

data class DataAuthUser(
    val id: Long,
    val email: String,
    val apiKey: String,
)