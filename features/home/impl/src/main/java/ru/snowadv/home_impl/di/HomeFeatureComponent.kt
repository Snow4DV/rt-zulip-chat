package ru.snowadv.home_impl.di

import dagger.Component
import ru.snowadv.home_api.di.HomeFeatureAPI
import ru.snowadv.home_api.di.HomeFeatureDependencies
import ru.snowadv.home_impl.presentation.home.HomeFragment
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [HomeFeatureDependencies::class], modules = [HomeFeatureModule::class])
@PerScreen
internal interface HomeFeatureComponent : HomeFeatureAPI {
    fun inject(fragment: HomeFragment)
    companion object {
        fun initAndGet(deps: HomeFeatureDependencies): HomeFeatureComponent {
            return DaggerHomeFeatureComponent.builder()
                .homeFeatureDependencies(deps)
                .build()
        }
    }
}