package ru.snowadv.users_data.di.holder

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.users_domain_api.repository.UserRepository

interface UsersDataAPI : BaseModuleAPI {
    val userRepo: UserRepository
}