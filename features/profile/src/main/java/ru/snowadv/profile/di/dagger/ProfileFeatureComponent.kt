package ru.snowadv.profile.di.dagger

import dagger.Component
import ru.snowadv.module_injector.general.PerScreen
import ru.snowadv.profile.di.holder.ProfileFeatureAPI
import ru.snowadv.profile.di.holder.ProfileFeatureDependencies
import ru.snowadv.profile.presentation.profile.ProfileFragment
import ru.snowadv.profile.presentation.profile.elm.ProfileReducerElm
import ru.snowadv.profile.presentation.profile.elm.ProfileStoreFactoryElm

@Component(dependencies = [ProfileFeatureDependencies::class], modules = [ProfileFeatureModule::class])
@PerScreen
internal interface ProfileFeatureComponent : ProfileFeatureAPI {
    val storeFactory: ProfileStoreFactoryElm
    fun inject(fragment: ProfileFragment)
    companion object {
        fun initAndGet(deps: ProfileFeatureDependencies): ProfileFeatureComponent {
            return DaggerProfileFeatureComponent.builder()
                .profileFeatureDependencies(deps)
                .build()
        }
    }
}