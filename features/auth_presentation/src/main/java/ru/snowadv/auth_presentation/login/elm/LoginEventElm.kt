package ru.snowadv.auth_presentation.login.elm

sealed interface LoginEventElm {

    sealed interface Ui : LoginEventElm {
        data object Init : Ui
        data object OnLoginButtonClicked : Ui
        data class ChangedEmailField(val newEmail: String) : Ui
        data class ChangedPasswordField(val newPassword: String) : Ui
    }

    sealed interface Internal : LoginEventElm {
        data object Loading : Internal
        data class ValidationError(val isEmailValid: Boolean, val isPasswordValid: Boolean) :
            Internal
        data class AuthError(val throwable: Throwable) : Internal
        data object LoggedInSuccessfully : Internal
    }

}