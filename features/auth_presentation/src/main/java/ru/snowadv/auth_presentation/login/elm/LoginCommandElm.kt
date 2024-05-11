package ru.snowadv.auth_presentation.login.elm

sealed interface LoginCommandElm {
    data class Login(
        val email: String,
        val password: String,
    ) : LoginCommandElm

    data object ClearAuth : LoginCommandElm
}