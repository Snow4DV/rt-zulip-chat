package ru.snowadv.profile_impl.di

import dagger.Component
import ru.snowadv.module_injector.general.PerFeature
import ru.snowadv.module_injector.general.PerScreen
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileStoreFactoryElm
import ru.snowadv.profile_api.di.ProfileFeatureAPI
import ru.snowadv.profile_api.di.ProfileFeatureDependencies

@Component(dependencies = [ProfileFeatureDependencies::class], modules = [ProfileFeatureModule::class])
@PerScreen
internal interface ProfileFeatureComponent : ProfileFeatureAPI {
    val storeFactory: ProfileStoreFactoryElm
    companion object {
        fun initAndGet(deps: ProfileFeatureDependencies): ProfileFeatureComponent {
            return DaggerProfileFeatureComponent.builder()
                .profileFeatureDependencies(deps)
                .build()
        }
    }
}