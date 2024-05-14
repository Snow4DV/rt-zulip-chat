package ru.snowadv.chat.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

sealed class ChatPaginationStatus(override val id: Int): DelegateItem {
    data object Loading: ChatPaginationStatus(1)
    data object Error: ChatPaginationStatus(2)
    data object HasMore: ChatPaginationStatus(3)
    data object LoadedAll: ChatPaginationStatus(4)
    data object None: ChatPaginationStatus(5)
}