package ru.snowadv.auth_domain_api.di

import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.auth_domain_api.use_case.AuthorizeUseCase
import ru.snowadv.auth_domain_api.use_case.ClearAuthUseCase
import ru.snowadv.auth_domain_api.use_case.ValidateEmailUseCase
import ru.snowadv.auth_domain_api.use_case.ValidatePasswordUseCase
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AuthDomainAPI : BaseModuleAPI {
    val authUseCase: AuthorizeUseCase
    val clearAuthUseCase: ClearAuthUseCase
    val validateEmailUseCase: ValidateEmailUseCase
    val validatePasswordUseCase: ValidatePasswordUseCase
}