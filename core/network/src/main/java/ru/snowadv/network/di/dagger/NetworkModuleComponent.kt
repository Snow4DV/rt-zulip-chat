package ru.snowadv.network.di.dagger

import dagger.Component
import ru.snowadv.module_injector.general.PerFeature
import ru.snowadv.network.di.holder.NetworkModuleAPI
import ru.snowadv.network.di.holder.NetworkModuleDependencies

@Component(dependencies = [NetworkModuleDependencies::class], modules = [NetworkModule::class])
@PerFeature
internal interface NetworkModuleComponent : NetworkModuleAPI {
    companion object {
        fun initAndGet(deps: NetworkModuleDependencies): NetworkModuleComponent {
            return DaggerNetworkModuleComponent.builder()
                .networkModuleDependencies(deps)
                .build()
        }
    }
}