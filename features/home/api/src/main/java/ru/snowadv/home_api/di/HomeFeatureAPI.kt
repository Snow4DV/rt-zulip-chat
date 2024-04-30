package ru.snowadv.home_api.di

import ru.snowadv.home_api.presentation.feature.HomeScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface HomeFeatureAPI : BaseModuleAPI {
    val screenFactory: HomeScreenFactory
}