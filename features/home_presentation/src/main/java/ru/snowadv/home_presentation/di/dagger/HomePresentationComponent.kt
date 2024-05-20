package ru.snowadv.home_presentation.di.dagger

import dagger.Component
import ru.snowadv.home_presentation.di.holder.HomePresentationAPI
import ru.snowadv.home_presentation.di.holder.HomePresentationDependencies
import ru.snowadv.home_presentation.home.HomeFragment
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [HomePresentationDependencies::class], modules = [HomePresentationModule::class])
@PerScreen
internal interface HomePresentationComponent : HomePresentationAPI {
    fun inject(fragment: HomeFragment)
    companion object {
        fun initAndGet(deps: HomePresentationDependencies): HomePresentationComponent {
            return DaggerHomePresentationComponent.builder()
                .homePresentationDependencies(deps)
                .build()
        }
    }
}