package ru.snowadv.auth_domain_api.model

data class AuthUser(
    val id: Long,
    val email: String,
    val apiKey: String,
)