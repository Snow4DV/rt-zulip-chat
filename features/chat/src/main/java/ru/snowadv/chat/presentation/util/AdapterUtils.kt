package ru.snowadv.chat.presentation.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.snowadv.chat.presentation.model.ChatDate
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.util.ChatMappers.toUiChatMessage
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.chat.domain.model.ChatMessage as DomainChatMessage

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

    /**
     * This function submits new adapter and keeps it scrolled to the bottom if it is already at the bottom
     */
    fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.submitListMaintainingBottomPos(
        list: List<T>?,
        recycler: RecyclerView,
        callback: (() -> Unit)? = null,
    ) {
        submitList(list) {
            when {
                itemCount <= 0 -> return@submitList
                (recycler.layoutManager as? LinearLayoutManager)?.findLastCompletelyVisibleItemPosition() == itemCount - 1 -> {
                    recycler.scrollToPosition(itemCount - 1)
                }
            }
            callback?.invoke()
        }
    }

    fun RecyclerView.LayoutManager.isScrolledToTop(): Boolean {
        return when {
            itemCount <= 0 -> true
            this is LinearLayoutManager -> this.findFirstCompletelyVisibleItemPosition() == 0
            else -> false
        }
    }

}