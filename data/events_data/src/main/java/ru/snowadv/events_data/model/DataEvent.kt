package ru.snowadv.events_data.model

sealed class DataEvent {
    abstract val id: Long
    data class MessageEvent(
        override val id: Long,
        val message: DataMessage
    ): DataEvent()
}