package ru.snowadv.chat.presentation.util

import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatDate
import ru.snowadv.presentation.adapter.DelegateItem

internal fun List<ChatMessage>.mapToAdapterMessagesAndDates(currentUserId: Long): List<DelegateItem> {
    val messagesMap = this
        .groupBy { it.sentAt.toLocalDate() }
        .mapValues {
            it.value
                .sortedBy { message -> message.sentAt }
                .map { message -> message.toUiChatMessage(currentUserId) }
        }
        .toSortedMap()

    return buildList {
        messagesMap.forEach {
            add(ChatDate(it.key))
            addAll(it.value)
        }
    }
}

