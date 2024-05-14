package ru.snowadv.channels_impl.presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.channels_impl.presentation.channel_list.ChannelListFragment
import ru.snowadv.channels_impl.presentation.stream_list.StreamListFragment
import javax.inject.Inject

@Reusable
internal class ChannelsScreenFactoryImpl @Inject constructor() : ChannelsScreenFactory {
    override fun create(): Fragment = ChannelListFragment.newInstance()
}