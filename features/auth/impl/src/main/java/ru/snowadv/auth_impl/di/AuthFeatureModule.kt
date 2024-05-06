package ru.snowadv.auth_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.auth_api.domain.repository.AuthRepository
import ru.snowadv.auth_api.domain.use_case.AuthorizeUseCase
import ru.snowadv.auth_api.domain.use_case.ClearAuthUseCase
import ru.snowadv.auth_api.domain.use_case.ValidateEmailUseCase
import ru.snowadv.auth_api.domain.use_case.ValidatePasswordUseCase
import ru.snowadv.auth_api.presentation.AuthScreenFactory
import ru.snowadv.auth_impl.data.AuthRepositoryImpl
import ru.snowadv.auth_impl.domain.use_case.AuthorizeUseCaseImpl
import ru.snowadv.auth_impl.domain.use_case.ClearAuthUseCaseImpl
import ru.snowadv.auth_impl.domain.use_case.ValidateEmailUseCaseImpl
import ru.snowadv.auth_impl.domain.use_case.ValidatePasswordUseCaseImpl
import ru.snowadv.auth_impl.presentation.feature.AuthScreenFactoryImpl
import ru.snowadv.auth_impl.presentation.login.elm.LoginActorElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginCommandElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginEffectElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginEventElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginReducerElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface AuthFeatureModule {
    @Binds
    fun bindAuthRepoImpl(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
    @Binds
    fun bindAuthorizeUseCaseImpl(authorizeUseCaseImpl: AuthorizeUseCaseImpl): AuthorizeUseCase
    @Binds
    fun bindValidateEmailUseCaseImpl(validateEmailUseCaseImpl: ValidateEmailUseCaseImpl): ValidateEmailUseCase
    @Binds
    fun bindValidatePasswordUseCaseImpl(validatePasswordUseCaseImpl: ValidatePasswordUseCaseImpl): ValidatePasswordUseCase
    @Binds
    fun bindClearAuthUseCaseImpl(clearAuthUseCaseImpl: ClearAuthUseCaseImpl): ClearAuthUseCase
    @Binds
    fun bindLoginActorElm(loginActorElm: LoginActorElm): Actor<LoginCommandElm, LoginEventElm>
    @Binds
    fun bindLoginReducerElm(loginReducerElm: LoginReducerElm): ScreenDslReducer<LoginEventElm, LoginEventElm.Ui, LoginEventElm.Internal, LoginStateElm, LoginEffectElm, LoginCommandElm>
    @Binds
    fun bindAuthScreenFactoryImpl(authScreenFactoryImpl: AuthScreenFactoryImpl): AuthScreenFactory
}