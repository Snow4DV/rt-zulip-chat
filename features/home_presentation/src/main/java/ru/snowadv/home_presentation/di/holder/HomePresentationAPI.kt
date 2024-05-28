package ru.snowadv.home_presentation.di.holder

import ru.snowadv.home_presentation.api.HomeScreenFactory
import ru.snowadv.module_injector.module.BaseModuleAPI

interface HomePresentationAPI : BaseModuleAPI {
    val screenFactory: HomeScreenFactory
}