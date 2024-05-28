package ru.snowadv.chat_domain_api.model

sealed interface ChatPaginationStatus {
    data object Loading: ChatPaginationStatus
    data object Error: ChatPaginationStatus
    data object HasMore: ChatPaginationStatus
    data object LoadedAll: ChatPaginationStatus
    data object None: ChatPaginationStatus
}