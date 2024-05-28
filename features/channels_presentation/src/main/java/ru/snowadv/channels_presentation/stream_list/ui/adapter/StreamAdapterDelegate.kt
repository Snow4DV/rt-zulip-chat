package ru.snowadv.channels_presentation.stream_list.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.channels_presentation.databinding.ItemStreamBinding
import ru.snowadv.channels_presentation.stream_list.ui.model.UiStream
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal class StreamAdapterDelegate(
    private val onStreamClickListener: ((UiStream) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<UiStream, StreamAdapterDelegate.StreamViewHolder, UiStream.Payload>() {
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

        fun bind(stream: UiStream) = with(binding) {
            expandStreamButton.isSelected = stream.expanded
            streamNameText.text =
                root.context.getString(ru.snowadv.presentation.R.string.stream_title, stream.name)
        }

        fun handlePayload(payload: UiStream.Payload) {
            when (payload) {
                is UiStream.Payload.ExpandedChanged -> bindExpanded(payload.expanded)
            }
        }

        private fun bindExpanded(expanded: Boolean) = with(binding) {
            expandStreamButton.isSelected = expanded
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is UiStream
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
        item: UiStream,
        holder: StreamViewHolder,
        payloads: List<UiStream.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.forEach(holder::handlePayload)
        }
    }
}