package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto(
    @SerialName("code")
    val code: String? = null,
    @SerialName("msg")
    val msg: String? = null,
)