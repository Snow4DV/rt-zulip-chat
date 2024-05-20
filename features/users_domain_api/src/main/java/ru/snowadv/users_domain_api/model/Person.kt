package ru.snowadv.users_domain_api.model

data class Person(
    val id: Long,
    val fullName: String,
    val email: String,
    val avatarUrl: String?,
    val status: Status
) {
    enum class Status(val apiName: String) {
        ONLINE("active"),
        IDLE("idle"),
        OFFLINE("offline"),
        UNKNOWN(""),
    }
}