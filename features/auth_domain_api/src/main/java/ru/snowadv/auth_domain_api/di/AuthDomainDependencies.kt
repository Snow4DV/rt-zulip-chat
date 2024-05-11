package ru.snowadv.auth_domain_api.di

import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AuthDomainDependencies : BaseModuleDependencies {
    val repo: AuthRepository
}