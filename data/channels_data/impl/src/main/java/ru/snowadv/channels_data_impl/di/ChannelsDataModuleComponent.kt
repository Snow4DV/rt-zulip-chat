package ru.snowadv.channels_data_impl.di

import dagger.Component
import ru.snowadv.channels_data_api.di.ChannelsDataModuleAPI
import ru.snowadv.channels_data_api.di.ChannelsDataModuleDependencies
import javax.inject.Singleton

@Singleton
@Component(dependencies = [ChannelsDataModuleDependencies::class], modules = [ChannelsDataModule::class])
internal interface ChannelsDataModuleComponent : ChannelsDataModuleAPI {
    companion object {
        fun initAndGet(deps: ChannelsDataModuleDependencies): ChannelsDataModuleComponent {
            return DaggerChannelsDataModuleComponent.builder()
                .channelsDataModuleDependencies(deps)
                .build()
        }
    }
}