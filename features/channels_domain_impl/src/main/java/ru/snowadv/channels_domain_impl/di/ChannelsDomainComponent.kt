package ru.snowadv.channels_domain_impl.di

import dagger.Component
import ru.snowadv.channels_domain_api.di.ChannelsDomainAPI
import ru.snowadv.channels_domain_api.di.ChannelsDomainDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChannelsDomainDependencies::class], modules = [ChannelsDomainModule::class])
@PerScreen
internal interface ChannelsDomainComponent : ChannelsDomainAPI {
    companion object {
        fun initAndGet(deps: ChannelsDomainDependencies): ChannelsDomainComponent {
            return DaggerChannelsDomainComponent.builder()
                .channelsDomainDependencies(deps)
                .build()
        }
    }
}