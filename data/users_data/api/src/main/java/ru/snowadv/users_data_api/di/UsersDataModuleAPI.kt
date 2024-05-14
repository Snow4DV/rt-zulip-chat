package ru.snowadv.users_data_api.di

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.users_data_api.UserDataRepository

interface UsersDataModuleAPI : BaseModuleAPI {
    val userDataRepo: UserDataRepository
}