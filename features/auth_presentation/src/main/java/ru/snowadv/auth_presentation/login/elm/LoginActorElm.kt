package ru.snowadv.auth_presentation.login.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.auth_presentation.navigation.AuthRouter
import ru.snowadv.auth_domain_api.use_case.AuthorizeUseCase
import ru.snowadv.auth_domain_api.use_case.ClearAuthUseCase
import ru.snowadv.auth_domain_api.use_case.ValidateEmailUseCase
import ru.snowadv.auth_domain_api.use_case.ValidatePasswordUseCase
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class LoginActorElm @Inject constructor(
    private val router: AuthRouter,
    private val authUseCase: AuthorizeUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val clearAuthUseCase: ClearAuthUseCase,
) : Actor<LoginCommandElm, LoginEventElm>() {
    override fun execute(command: LoginCommandElm): Flow<LoginEventElm> = when (command) {
        is LoginCommandElm.Login -> flow {
            val isEmailValid = validateEmailUseCase(command.email)
            val isPasswordValid = validatePasswordUseCase(command.password)

            if (!isEmailValid || !isPasswordValid) {
                emit(LoginEventElm.Internal.ValidationError(isEmailValid, isPasswordValid))
                return@flow
            }

            authUseCase(command.email, command.password).map { res ->
                when(res) {
                    is Resource.Error -> LoginEventElm.Internal.AuthError(res.throwable)
                    is Resource.Loading -> LoginEventElm.Internal.Loading
                    is Resource.Success -> {
                        router.goToHomeAfterAuth()
                        LoginEventElm.Internal.LoggedInSuccessfully
                    }
                }
            }.collect { event -> emit(event) }
        }

        LoginCommandElm.ClearAuth -> flow { clearAuthUseCase() }
    }
}