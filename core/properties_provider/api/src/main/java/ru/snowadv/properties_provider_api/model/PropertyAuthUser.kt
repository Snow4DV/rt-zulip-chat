package ru.snowadv.properties_provider_api.model

data class PropertyAuthUser(
    val id: Long,
    val email: String,
    val apiKey: String,
)