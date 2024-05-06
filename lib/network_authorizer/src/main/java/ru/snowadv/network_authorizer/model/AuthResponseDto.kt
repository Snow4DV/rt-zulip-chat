package ru.snowadv.network_authorizer.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    @SerialName("api_key")
    val apiKey: String,
    @SerialName("email")
    val email: String,
    @SerialName("msg")
    val msg: String,
    @SerialName("result")
    val result: String,
    @SerialName("user_id")
    val userId: Long
)