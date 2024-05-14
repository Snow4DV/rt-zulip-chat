package ru.snowadv.network.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.BadAuthBehavior

interface NetworkModuleDependencies : BaseModuleDependencies {
    val badAuthBehavior: BadAuthBehavior
    val authProvider: AuthProvider
    val json: Json
}