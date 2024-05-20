package ru.snowadv.profile_presentation.di.holder

import ru.snowadv.module_injector.module.BaseModuleAPI
import ru.snowadv.profile_presentation.api.ProfileScreenFactory

interface ProfilePresentationAPI : BaseModuleAPI {
    val screenFactory: ProfileScreenFactory
}