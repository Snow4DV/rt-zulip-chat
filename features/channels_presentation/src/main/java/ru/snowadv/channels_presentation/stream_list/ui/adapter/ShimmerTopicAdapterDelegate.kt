package ru.snowadv.channels_presentation.stream_list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels_presentation.databinding.ItemTopicShimmerBinding
import ru.snowadv.channels_presentation.stream_list.ui.model.UiShimmerTopic
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal class ShimmerTopicAdapterDelegate :
    DelegationItemAdapterDelegate<UiShimmerTopic, ShimmerTopicAdapterDelegate.TopicViewHolder, Nothing>() {
    internal inner class TopicViewHolder(
        binding: ItemTopicShimmerBinding,
    ) :
        ViewHolder(binding.root)

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is UiShimmerTopic
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return TopicViewHolder(
            ItemTopicShimmerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        item: UiShimmerTopic,
        holder: TopicViewHolder,
        payloads: List<Nothing>
    ) {

    }
}