package ru.snowadv.channels_data_api.model

data class DataTopic(
    val uniqueId: String, // ID should be generated using stream's ID
    val name: String,
    val streamId: Long,
)