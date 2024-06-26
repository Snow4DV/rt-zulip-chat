package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionDetailsRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
)