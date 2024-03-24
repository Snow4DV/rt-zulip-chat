package ru.snowadv.chat.presentation.chat.state

import ru.snowadv.presentation.adapter.DelegateItem

data class ChatScreenState(
    val loading: Boolean = true,
    val stream: String,
    val topic: String,
    val messagesAndDates: List<DelegateItem> = emptyList(),
    val messageField: String = "",
)