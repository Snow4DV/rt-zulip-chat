package ru.snowadv.users_data.model

data class DataUser(
    val id: Long,
    val fullName: String,
    val email: String,
    val avatarUrl: String?,
    val status: DataUserStatus,
)