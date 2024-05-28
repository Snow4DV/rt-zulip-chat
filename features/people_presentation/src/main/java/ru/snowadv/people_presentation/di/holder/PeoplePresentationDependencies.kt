package ru.snowadv.people_presentation.di.holder

import android.content.Context
import coil.ImageLoader
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.people_presentation.navigation.PeopleRouter
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase

interface PeoplePresentationDependencies : BaseModuleDependencies {
    val peopleRouter: PeopleRouter

    val getPeopleUseCase: GetPeopleUseCase
    val listenToPeopleRouter: ListenToPeoplePresenceEventsUseCase

    val appContext: Context
}