package ru.snowadv.chat.presentation.util

import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatMessage as DomainChatMessage
import ru.snowadv.chat.presentation.model.ChatDate
import ru.snowadv.presentation.adapter.DelegateItem

internal fun List<DomainChatMessage>.mapToAdapterMessagesAndDates(currentUserId: Long): List<DelegateItem> {
    return this.map { it.toUiChatMessage(currentUserId) }.mapToAdapterMessagesAndDates()
}


internal fun List<ChatMessage>.mapToAdapterMessagesAndDates(): List<DelegateItem> {
    val messagesMap = this
        .groupBy { it.sentAt.toLocalDate() }
        .mapValues {
            it.value
                .sortedBy { message -> message.sentAt }
        }
        .toSortedMap()

    return buildList {
        messagesMap.forEach {
            add(ChatDate(it.key))
            addAll(it.value)
        }
    }
}

