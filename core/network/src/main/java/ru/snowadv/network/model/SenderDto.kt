package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SenderDto(
    @SerialName("email")
    val email: String,
    @SerialName("user_id")
    val userId: Long,
)