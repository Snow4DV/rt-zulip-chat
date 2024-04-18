package ru.snowadv.channels.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels.R
import ru.snowadv.channels.databinding.ItemTopicBinding
import ru.snowadv.channels.databinding.ItemTopicShimmerBinding
import ru.snowadv.channels.presentation.model.ShimmerTopic
import ru.snowadv.channels.presentation.model.Topic
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import kotlin.math.abs

internal class ShimmerTopicAdapterDelegate :
    DelegationItemAdapterDelegate<ShimmerTopic, ShimmerTopicAdapterDelegate.TopicViewHolder, Nothing>() {
    internal inner class TopicViewHolder(
        binding: ItemTopicShimmerBinding,
    ) :
        ViewHolder(binding.root)

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is ShimmerTopic
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
        item: ShimmerTopic,
        holder: TopicViewHolder,
        payloads: List<Nothing>
    ) {

    }
}