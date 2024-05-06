package ru.snowadv.network_authorizer.di.dagger

import dagger.Component
import ru.snowadv.module_injector.general.PerFeature
import ru.snowadv.network_authorizer.di.holder.NetworkAuthorizerLibAPI
import ru.snowadv.network_authorizer.di.holder.NetworkAuthorizerLibDependencies

@Component(dependencies = [NetworkAuthorizerLibDependencies::class], modules = [NetworkAuthorizerLibModule::class])
@PerFeature
internal interface NetworkAuthorizerLibComponent : NetworkAuthorizerLibAPI {
    companion object {
        fun initAndGet(deps: NetworkAuthorizerLibDependencies): NetworkAuthorizerLibComponent {
            return DaggerNetworkAuthorizerLibComponent.builder()
                .networkAuthorizerLibDependencies(deps)
                .build()
        }
    }
}