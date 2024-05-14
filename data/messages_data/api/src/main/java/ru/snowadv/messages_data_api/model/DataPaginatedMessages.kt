package ru.snowadv.messages_data_api.model

data class DataPaginatedMessages(
    val messages: List<DataMessage>,
    val foundAnchor: Boolean,
    val foundOldest: Boolean,
    val foundNewest: Boolean,
)