package ru.snowadv.channels_impl.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels_impl.databinding.ItemStreamBinding
import ru.snowadv.channels_impl.presentation.model.Stream
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal class StreamAdapterDelegate(
    private val onStreamClickListener: ((Stream) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<Stream, StreamAdapterDelegate.StreamViewHolder, Stream.Payload>() {
    internal inner class StreamViewHolder(private val binding: ItemStreamBinding) :
        ViewHolder(binding.root) {
        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) = with(binding) {
            root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onStreamClickListener?.invoke(it)
                    }
                }
            }
        }

        fun bind(stream: Stream) = with(binding) {
            expandStreamButton.isSelected = stream.expanded
            streamNameText.text =
                root.context.getString(ru.snowadv.presentation.R.string.stream_title, stream.name)
        }

        fun handlePayload(payload: Stream.Payload) {
            when (payload) {
                is Stream.Payload.ExpandedChanged -> bindExpanded(payload.expanded)
            }
        }

        private fun bindExpanded(expanded: Boolean) = with(binding) {
            expandStreamButton.isSelected = expanded
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is Stream
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return StreamViewHolder(
            ItemStreamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { streamViewHolder ->
            streamViewHolder.initClickListeners(getCurrentList)
        }
    }

    override fun onBindViewHolder(
        item: Stream,
        holder: StreamViewHolder,
        payloads: List<Stream.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.forEach(holder::handlePayload)
        }
    }
}