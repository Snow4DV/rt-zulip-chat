package ru.snowadv.network.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.BadAuthBehavior

interface NetworkLibDependencies : BaseModuleDependencies {
    val badAuthBehavior: BadAuthBehavior
    val authProvider: AuthProvider
    val json: Json
}