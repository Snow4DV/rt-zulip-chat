package ru.snowadv.channels_impl.di

import dagger.Component
import ru.snowadv.channels_api.di.ChannelsFeatureAPI
import ru.snowadv.channels_api.di.ChannelsFeatureDependencies
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListStoreFactoryElm
import ru.snowadv.channels_impl.presentation.stream_list.elm.StreamListStoreFactoryElm
import ru.snowadv.module_injector.general.PerScreen
@Component(dependencies = [ChannelsFeatureDependencies::class], modules = [ChannelsFeatureModule::class])
@PerScreen
internal interface ChannelsFeatureComponent : ChannelsFeatureAPI {
    val channelListStoreFactory: ChannelListStoreFactoryElm
    val streamListStoreFactory: StreamListStoreFactoryElm
    companion object {
        fun initAndGet(deps: ChannelsFeatureDependencies): ChannelsFeatureComponent {
            return DaggerChannelsFeatureComponent.builder()
                .channelsFeatureDependencies(deps)
                .build()
        }
    }
}