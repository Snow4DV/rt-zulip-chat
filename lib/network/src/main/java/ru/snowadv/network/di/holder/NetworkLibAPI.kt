package ru.snowadv.network.di.holder

import okhttp3.OkHttpClient
import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network.api.ZulipApi

interface NetworkLibAPI : BaseModuleAPI {
    val zulipApi: ZulipApi
    val okHttpClient: OkHttpClient
}