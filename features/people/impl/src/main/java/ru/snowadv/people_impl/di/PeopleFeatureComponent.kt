package ru.snowadv.people_impl.di

import dagger.Component
import ru.snowadv.module_injector.general.PerFeature
import ru.snowadv.module_injector.general.PerScreen
import ru.snowadv.people_api.di.PeopleFeatureAPI
import ru.snowadv.people_api.di.PeopleFeatureDependencies
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListStoreFactoryElm

@Component(dependencies = [PeopleFeatureDependencies::class], modules = [PeopleFeatureModule::class])
@PerScreen
internal interface PeopleFeatureComponent : PeopleFeatureAPI {
    val storeFactory: PeopleListStoreFactoryElm
    companion object {
        fun initAndGet(deps: PeopleFeatureDependencies): PeopleFeatureComponent {
            return DaggerPeopleFeatureComponent.builder()
                .peopleFeatureDependencies(deps)
                .build()
        }
    }
}