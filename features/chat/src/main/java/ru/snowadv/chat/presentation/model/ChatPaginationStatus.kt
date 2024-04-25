package ru.snowadv.chat.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

sealed class ChatPaginationStatus: DelegateItem {
    data object Loading: ChatPaginationStatus() {
        override val id: Int = 1
    }
    data object Error: ChatPaginationStatus() {
        override val id: Int = 2
    }
    data object HasMore: ChatPaginationStatus() {
        override val id: Int = 3
    }
    data object LoadedAll: ChatPaginationStatus() {
        override val id: Int = 4
    }

    data object None: ChatPaginationStatus() {
        override val id: Int = 5
    }
}