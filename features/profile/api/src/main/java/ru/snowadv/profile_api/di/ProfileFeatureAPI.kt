package ru.snowadv.profile_api.di

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.profile_api.presentation.ProfileScreenFactory

interface ProfileFeatureAPI : BaseModuleAPI {
    val screenFactory: ProfileScreenFactory
}