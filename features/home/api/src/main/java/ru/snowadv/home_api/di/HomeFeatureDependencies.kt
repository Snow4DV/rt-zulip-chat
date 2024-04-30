package ru.snowadv.home_api.di

import ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface HomeFeatureDependencies : BaseModuleDependencies {
    val homeScreenFactory: InnerHomeScreenFactory
}