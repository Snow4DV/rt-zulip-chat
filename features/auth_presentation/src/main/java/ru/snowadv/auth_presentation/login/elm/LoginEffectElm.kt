package ru.snowadv.auth_presentation.login.elm

sealed interface LoginEffectElm {
    data class ShowInternetErrorWithRetry(val retryEvent: LoginEventElm): LoginEffectElm
    data class ShowValidationError(val invalidEmail: Boolean, val invalidPassword: Boolean):
        LoginEffectElm
}