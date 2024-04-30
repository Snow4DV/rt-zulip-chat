package ru.snowadv.chat_impl.presentation.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat_impl.presentation.model.ChatDate
import ru.snowadv.chat_impl.presentation.model.ChatMessage
import ru.snowadv.chat_impl.presentation.util.ChatMappers.toUiChatMessage
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.chat_api.domain.model.ChatMessage as DomainChatMessage

internal object AdapterUtils {
    fun List<DomainChatMessage>.mapToUiAdapterMessagesAndDates(): List<DelegateItem> {
        return this.map { it.toUiChatMessage() }.mapToAdapterMessagesAndDates()
    }

    private fun List<ChatMessage>.mapToAdapterMessagesAndDates(): List<DelegateItem> {
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
}