package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersResponseDto(
    @SerialName("members")
    val users: List<UserResponseDto>,
)

@Serializable
data class SingleUserResponseDto(
    @SerialName("user")
    val user: UserResponseDto,
)

@Serializable
data class UserResponseDto(
    @SerialName("user_id")
    val id: Long,
    @SerialName("full_name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    @SerialName("is_active")
    val isActive: Boolean
)