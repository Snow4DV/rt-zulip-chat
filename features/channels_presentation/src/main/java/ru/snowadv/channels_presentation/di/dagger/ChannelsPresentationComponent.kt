package ru.snowadv.channels_presentation.di.dagger

import dagger.Component
import ru.snowadv.channels_presentation.channel_list.ChannelListFragment
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationAPI
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationDependencies
import ru.snowadv.channels_presentation.stream_creator.StreamCreatorBottomSheetDialog
import ru.snowadv.channels_presentation.stream_list.ui.StreamListFragment
import ru.snowadv.channels_presentation.stream_list.ui.StreamListRenderer
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [ChannelsPresentationDependencies::class], modules = [ChannelsPresentationModule::class])
@PerScreen
internal interface ChannelsPresentationComponent : ChannelsPresentationAPI {
    fun inject(streamsRenderer: StreamListRenderer)
    fun inject(streamListFragment: StreamListFragment)
    fun inject(channelListFragment: ChannelListFragment)
    fun inject(streamCreatorBottomSheetDialog: StreamCreatorBottomSheetDialog)
    companion object {
        fun initAndGet(deps: ChannelsPresentationDependencies): ChannelsPresentationComponent {
            return DaggerChannelsPresentationComponent.builder()
                .channelsPresentationDependencies(deps)
                .build()
        }
    }
}