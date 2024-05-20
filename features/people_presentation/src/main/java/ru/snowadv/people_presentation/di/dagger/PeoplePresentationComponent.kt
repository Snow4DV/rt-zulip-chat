package ru.snowadv.people_presentation.di.dagger

import dagger.Component
import ru.snowadv.module_injector.general.PerScreen
import ru.snowadv.people_presentation.di.holder.PeoplePresentationAPI
import ru.snowadv.people_presentation.di.holder.PeoplePresentationDependencies
import ru.snowadv.people_presentation.ui.PeopleFragment
import ru.snowadv.people_presentation.ui.PeopleRenderer

@Component(dependencies = [PeoplePresentationDependencies::class], modules = [PeoplePresentationModule::class])
@PerScreen
internal interface PeoplePresentationComponent : PeoplePresentationAPI {

    fun inject(peopleFragment: PeopleFragment)
    fun inject(peopleRenderer: PeopleRenderer)
    companion object {
        fun initAndGet(deps: PeoplePresentationDependencies): PeoplePresentationComponent {
            return DaggerPeoplePresentationComponent.builder()
                .peoplePresentationDependencies(deps)
                .build()
        }
    }
}