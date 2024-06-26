package ru.snowadv.network.di.dagger

import dagger.Component
import ru.snowadv.module_injector.general.PerFeature
import ru.snowadv.network.di.holder.NetworkLibAPI
import ru.snowadv.network.di.holder.NetworkLibDependencies
import javax.inject.Singleton

@Component(dependencies = [NetworkLibDependencies::class], modules = [NetworkLibModule::class])
@Singleton
internal interface NetworkLibComponent : NetworkLibAPI {
    companion object {
        fun initAndGet(deps: NetworkLibDependencies): NetworkLibComponent {
            return DaggerNetworkLibComponent.builder()
                .networkLibDependencies(deps)
                .build()
        }
    }
}