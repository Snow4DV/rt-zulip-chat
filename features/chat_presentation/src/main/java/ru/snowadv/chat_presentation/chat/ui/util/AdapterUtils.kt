package ru.snowadv.chat_presentation.chat.ui.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat_presentation.chat.ui.model.ChatDate
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiChatMessage
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.chat_domain_api.model.ChatMessage as DomainChatMessage

internal object AdapterUtils {
    fun List<DomainChatMessage>.mapToUiAdapterMessagesAndDates(): List<DelegateItem> {
        return this.map { it.toUiChatMessage() }.mapToAdapterMessagesAndDates()
    }

    fun List<ChatMessage>.mapToAdapterMessagesAndDates(): List<DelegateItem> {
        val messagesMap = this
            .groupBy { it.sentAt.toLocalDate() }
            .mapValues {
                it.value
                    .sortedBy { message -> message.sentAt }
            }
            .toSortedMap()

        return buildList {
            messagesMap.forEach {
                add(ChatDate(it.value?.firstOrNull()?.sentAt ?: it.key.atStartOfDay()))
                addAll(it.value)
            }
        }
    }

    fun <T, VH : RecyclerView.ViewHolder?> ListAdapter<T, VH>.submitListAndKeepScrolledToBottom(recycler: RecyclerView, list: List<T?>?, commitCallback: Runnable) {

        val shouldScrollToBottom = (recycler.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
            layoutManager.findLastCompletelyVisibleItemPosition() == currentList.lastIndex
        } ?: true
        this@submitListAndKeepScrolledToBottom.submitList(list) {
            if (shouldScrollToBottom && list != null) {
                recycler.smoothScrollToPosition(list.lastIndex)
            }
            commitCallback.run()
        }
    }
}