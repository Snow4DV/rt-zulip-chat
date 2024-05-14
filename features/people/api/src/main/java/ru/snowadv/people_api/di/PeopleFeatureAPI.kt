package ru.snowadv.people_api.di

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.people_api.presentation.PeopleScreenFactory

interface PeopleFeatureAPI : BaseModuleAPI {
    val screenFactory: PeopleScreenFactory
}