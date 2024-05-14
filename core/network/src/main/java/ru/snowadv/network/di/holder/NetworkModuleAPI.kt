package ru.snowadv.network.di.holder

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.ZulipApi

interface NetworkModuleAPI : BaseModuleAPI {
    val zulipApi: ZulipApi
}