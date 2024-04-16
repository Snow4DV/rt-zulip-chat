package ru.snowadv.message_data.model

data class DataPaginatedMessages(
    val messages: List<DataMessage>,
    val foundAnchor: Boolean,
    val foundOldest: Boolean,
    val foundNewest: Boolean,
)