package ru.snowadv.auth_presentation.login.elm

internal data class LoginStateElm(
    val loading: Boolean,
    val email: String,
    val password: String,
)