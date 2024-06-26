package ru.snowadv.chat_presentation.chat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.chat_presentation.databinding.ItemChatDateSplitterBinding
import ru.snowadv.chat_presentation.chat.ui.model.ChatDate
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate
import ru.snowadv.presentation.util.DateFormatter

internal class DateSplitterAdapterDelegate(
    private val dateFormatter: DateFormatter
) :
    DelegationItemAdapterDelegate<ChatDate, DateSplitterAdapterDelegate.ChatDateViewHolder, Nothing>() {
    internal inner class ChatDateViewHolder(private val binding: ItemChatDateSplitterBinding) :
        ViewHolder(binding.root) {
        fun bind(chatDate: ChatDate) = with(binding) {
            binding.dateText.text = dateFormatter.format(chatDate.dateTime.toLocalDate())
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is ChatDate
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return ChatDateViewHolder(
            ItemChatDateSplitterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        item: ChatDate,
        holder: ChatDateViewHolder,
        payloads: List<Nothing>
    ) {
        holder.bind(item)
    }
}