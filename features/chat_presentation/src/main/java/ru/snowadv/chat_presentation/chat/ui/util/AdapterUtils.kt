package ru.snowadv.chat_presentation.chat.ui.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat_presentation.chat.ui.model.ChatDate
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage
import ru.snowadv.chat_presentation.chat.ui.model.ChatTopic
import ru.snowadv.chat_presentation.chat.ui.util.ChatMappers.toUiChatMessage
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.chat_domain_api.model.ChatMessage as DomainChatMessage

internal object AdapterUtils {
    fun List<DomainChatMessage>.mapToUiAdapterMessagesAndDates(showTopics: Boolean): List<DelegateItem> {
        return this.map { it.toUiChatMessage() }.mapToAdapterMessagesAndDates(showTopics)
    }

    private fun List<ChatMessage>.mapToAdapterMessagesAndDates(showTopics: Boolean): List<DelegateItem> {
        val messagesMap = this
            .groupBy { it.sentAt.toLocalDate() }
            .mapValues {
                it.value
                    .sortedBy { message -> message.sentAt }
            }
            .toSortedMap()

        var lastTopic: String? = null

        return buildList {
            messagesMap.forEach { entry ->
                add(ChatDate(entry.value?.firstOrNull()?.sentAt ?: entry.key.atStartOfDay()))
                entry.value.forEach { chatMessage ->
                    if (lastTopic != chatMessage.topic && showTopics) {
                        add(ChatTopic(name = chatMessage.topic, firstMessageId = chatMessage.id))
                        lastTopic = chatMessage.topic
                    }
                    add(chatMessage)
                }
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