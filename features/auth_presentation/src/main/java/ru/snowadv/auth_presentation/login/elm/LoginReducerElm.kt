package ru.snowadv.auth_presentation.login.elm

import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class LoginReducerElm @Inject constructor():
    ScreenDslReducer<LoginEventElm, LoginEventElm.Ui, LoginEventElm.Internal, LoginStateElm, LoginEffectElm, LoginCommandElm>(
        uiEventClass = LoginEventElm.Ui::class,
        internalEventClass = LoginEventElm.Internal::class,
    ) {
    override fun Result.internal(event: LoginEventElm.Internal) {
        when(event) {
            is LoginEventElm.Internal.AuthError -> {
                state { copy(loading = false) }
                effects {
                    +LoginEffectElm.ShowInternetErrorWithRetry(LoginEventElm.Ui.OnLoginButtonClicked)
                }
            }
            LoginEventElm.Internal.Loading -> state { copy(loading = true) }
            LoginEventElm.Internal.LoggedInSuccessfully -> state { copy(loading = false) }
            is LoginEventElm.Internal.ValidationError -> effects {
                +LoginEffectElm.ShowValidationError(
                    invalidEmail = !event.isEmailValid,
                    invalidPassword = !event.isPasswordValid,
                )
            }
        }
    }

    override fun Result.ui(event: LoginEventElm.Ui) {
        when(event) {
            is LoginEventElm.Ui.ChangedEmailField -> state {
                copy(email = event.newEmail)
            }
            is LoginEventElm.Ui.ChangedPasswordField -> state {
                copy(password = event.newPassword)
            }
            LoginEventElm.Ui.OnLoginButtonClicked -> commands {
                +LoginCommandElm.Login(state.email, state.password)
            }

            LoginEventElm.Ui.Init -> commands {
                +LoginCommandElm.ClearAuth
            }
        }
    }

}