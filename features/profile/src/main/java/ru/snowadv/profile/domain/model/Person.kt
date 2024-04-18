package ru.snowadv.profile.domain.model

data class Person(
    val id: Long,
    val fullName: String,
    val email: String,
    val avatarUrl: String?,
    val status: Status
) {
    enum class Status {
        ONLINE,
        IDLE,
        OFFLINE,
    }
}