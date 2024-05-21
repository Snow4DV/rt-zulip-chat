package ru.snowadv.network_authorizer.di.holder

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.network_authorizer.api.ZulipAuthApi

interface NetworkAuthorizerLibAPI : BaseModuleAPI {
    val zulipAuthApi: ZulipAuthApi
}