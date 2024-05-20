package ru.snowadv.channels_presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.channels_presentation.api.ChannelsScreenFactory
import ru.snowadv.channels_presentation.channel_list.ChannelListFragment
import javax.inject.Inject

@Reusable
internal class ChannelsScreenFactoryImpl @Inject constructor() : ChannelsScreenFactory {
    override fun create(): Fragment = ChannelListFragment.newInstance()
}