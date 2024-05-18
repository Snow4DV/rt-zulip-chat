package ru.snowadv.chatapp.di.holder

import kotlinx.serialization.json.Json
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface TestAppModuleDependencies : BaseModuleDependencies {
    val json: Json
}