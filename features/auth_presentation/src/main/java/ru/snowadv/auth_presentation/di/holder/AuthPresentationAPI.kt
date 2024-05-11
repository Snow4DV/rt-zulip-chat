package ru.snowadv.auth_presentation.di.holder

import ru.snowadv.auth_domain_api.use_case.AuthorizeUseCase
import ru.snowadv.auth_domain_api.use_case.ClearAuthUseCase
import ru.snowadv.auth_domain_api.use_case.ValidateEmailUseCase
import ru.snowadv.auth_domain_api.use_case.ValidatePasswordUseCase
import ru.snowadv.auth_presentation.api.AuthScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AuthPresentationAPI : BaseModuleAPI {
    val screenFactory: AuthScreenFactory
}