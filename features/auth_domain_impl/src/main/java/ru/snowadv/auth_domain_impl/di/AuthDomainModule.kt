package ru.snowadv.auth_domain_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.auth_domain_api.use_case.AuthorizeUseCase
import ru.snowadv.auth_domain_api.use_case.ClearAuthUseCase
import ru.snowadv.auth_domain_api.use_case.ValidateEmailUseCase
import ru.snowadv.auth_domain_api.use_case.ValidatePasswordUseCase
import ru.snowadv.auth_domain_impl.use_case.AuthorizeUseCaseImpl
import ru.snowadv.auth_domain_impl.use_case.ClearAuthUseCaseImpl
import ru.snowadv.auth_domain_impl.use_case.ValidateEmailUseCaseImpl
import ru.snowadv.auth_domain_impl.use_case.ValidatePasswordUseCaseImpl

@Module
internal interface AuthDomainModule {
    @Binds
    fun bindAuthorizeUseCaseImpl(authorizeUseCaseImpl: AuthorizeUseCaseImpl): AuthorizeUseCase
    @Binds
    fun bindValidateEmailUseCaseImpl(validateEmailUseCaseImpl: ValidateEmailUseCaseImpl): ValidateEmailUseCase
    @Binds
    fun bindValidatePasswordUseCaseImpl(validatePasswordUseCaseImpl: ValidatePasswordUseCaseImpl): ValidatePasswordUseCase
    @Binds
    fun bindClearAuthUseCaseImpl(clearAuthUseCaseImpl: ClearAuthUseCaseImpl): ClearAuthUseCase
}