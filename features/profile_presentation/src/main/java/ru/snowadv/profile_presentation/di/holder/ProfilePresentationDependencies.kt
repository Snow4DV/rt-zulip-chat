package ru.snowadv.profile_presentation.di.holder

import android.content.Context
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.profile_presentation.navigation.ProfileRouter
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import ru.snowadv.users_domain_api.use_case.GetProfileUseCase
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase
import ru.snowadv.users_domain_api.use_case.ListenToProfilePresenceEventsUseCase

interface ProfilePresentationDependencies : BaseModuleDependencies {
    val profileRouter: ProfileRouter

    val getProfileUseCase: GetProfileUseCase
    val listenToProfileEventsUseCase: ListenToProfilePresenceEventsUseCase

    val appContext: Context
}