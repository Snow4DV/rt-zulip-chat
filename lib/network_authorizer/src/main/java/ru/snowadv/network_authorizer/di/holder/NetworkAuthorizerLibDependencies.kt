package ru.snowadv.network_authorizer.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface NetworkAuthorizerLibDependencies : BaseModuleDependencies {
    val json: Json
}