package ru.snowadv.channels_presentation.stream_list.ui.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class UiTopic(
    val uniqueId: String,
    val name: String,
    override val streamId: Long,
    val unreadMessagesCount: Int,
) : DelegateItem, StreamIdContainer {
    override val id: Any get() = uniqueId

    override fun getPayload(oldItem: DelegateItem): Any? {
        if (oldItem is UiTopic && oldItem.uniqueId == uniqueId && oldItem.name == name
            && oldItem.streamId == streamId && oldItem.unreadMessagesCount != unreadMessagesCount) {
            return Payload.MsgCounterChanged(unreadMessagesCount)
        }
        return null
    }

    sealed class Payload {
        class MsgCounterChanged(val newCounter: Int): Payload()
    }
}
