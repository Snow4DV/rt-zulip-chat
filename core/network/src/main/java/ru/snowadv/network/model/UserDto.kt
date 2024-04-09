package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersDto(
    @SerialName("members")
    val users: List<UserDto>,
)

@Serializable
data class SingleUserDto(
    @SerialName("user")
    val user: UserDto,
)

@Serializable
data class UserDto(
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