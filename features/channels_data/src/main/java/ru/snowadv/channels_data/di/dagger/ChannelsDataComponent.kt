package ru.snowadv.channels_data.di.dagger

import dagger.Component
import ru.snowadv.channels_data.di.holder.ChannelsDataAPI
import ru.snowadv.channels_data.di.holder.ChannelsDataDependencies
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChannelsDataDependencies::class], modules = [ChannelsDataModule::class])
@PerScreen
internal interface ChannelsDataComponent : ChannelsDataAPI {
    companion object {
        fun initAndGet(deps: ChannelsDataDependencies): ChannelsDataComponent {
            return DaggerChannelsDataComponent.builder()
                .channelsDataDependencies(deps)
                .build()
        }
    }
}