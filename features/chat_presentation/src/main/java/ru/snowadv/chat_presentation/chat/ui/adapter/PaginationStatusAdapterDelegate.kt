package ru.snowadv.chat_presentation.chat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.chat.ui.model.ChatPaginationStatus
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal class PaginationStatusAdapterDelegate(
    private val onPaginationStatusClick: ((ChatPaginationStatus) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<ChatPaginationStatus, PaginationStatusAdapterDelegate.PaginationStatusViewHolder, Nothing>() {
    internal inner class PaginationStatusViewHolder(private val stateHolder: FrameLayout) :
        ViewHolder(stateHolder) {
        fun bind(status: ChatPaginationStatus) {
            stateHolder.removeAllViews()
            when (status) {
                ChatPaginationStatus.Error -> R.layout.item_pagination_error

                ChatPaginationStatus.HasMore -> R.layout.item_pagination_has_more

                ChatPaginationStatus.Loading -> R.layout.item_message_view_incoming_shimmer

                ChatPaginationStatus.LoadedAll -> R.layout.item_pagination_all_done

                ChatPaginationStatus.None -> null
            }?.let { inflateLayout ->
                LayoutInflater.from(stateHolder.context)
                    .inflate(inflateLayout, stateHolder, true)
            }
        }

        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) {
            stateHolder.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let { paginationStatus ->
                        onPaginationStatusClick?.invoke(paginationStatus)
                    }
                }
            }
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is ChatPaginationStatus
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return PaginationStatusViewHolder(
            FrameLayout(parent.context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        ).also { it.initClickListeners(getCurrentList) }
    }

    override fun onBindViewHolder(
        item: ChatPaginationStatus,
        holder: PaginationStatusViewHolder,
        payloads: List<Nothing>
    ) {
        holder.bind(item)
    }
}