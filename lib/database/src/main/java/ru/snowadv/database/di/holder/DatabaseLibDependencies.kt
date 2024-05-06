package ru.snowadv.database.di.holder

import android.content.Context
import kotlinx.serialization.json.Json
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface DatabaseLibDependencies : BaseModuleDependencies {
    val appContext: Context
    val json: Json
}