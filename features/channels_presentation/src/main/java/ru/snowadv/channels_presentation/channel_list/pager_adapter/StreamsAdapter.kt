package ru.snowadv.channels_presentation.channel_list.pager_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.channels_presentation.stream_list.ui.StreamListFragment

internal class StreamsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val tabs: List<StreamType> = StreamType.entries,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment = StreamListFragment.newInstance(tabs[position])

    override fun getItemCount(): Int = tabs.size
}