package ru.snowadv.voiceapp.di.holder

import android.content.Context
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface AppModuleDependencies : BaseModuleDependencies {
    val appContext: Context
}