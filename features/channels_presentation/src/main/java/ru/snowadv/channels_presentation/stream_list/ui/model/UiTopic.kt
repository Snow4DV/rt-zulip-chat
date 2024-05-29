package ru.snowadv.channels_presentation.stream_list.ui.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class UiTopic(
    val uniqueId: String,
    val name: String,
    override val streamId: Long,
    val unreadMessagesCount: Int,
    val isLast: Boolean,
) : DelegateItem, StreamIdContainer {
    override val id: Any get() = uniqueId

    override fun getPayload(oldItem: DelegateItem): Any? {
        if (oldItem is UiTopic && oldItem.uniqueId == uniqueId && oldItem.name == name && oldItem.isLast == isLast
            && oldItem.streamId == streamId && oldItem.unreadMessagesCount != unreadMessagesCount) {
            return Payload.MsgCounterChanged(unreadMessagesCount)
        }
        if (oldItem is UiTopic && oldItem.uniqueId == uniqueId && oldItem.name == name && oldItem.isLast != isLast
            && oldItem.streamId == streamId && oldItem.unreadMessagesCount == unreadMessagesCount) {
            return Payload.SeparatorChanged(isLast)
        }
        return null
    }

    sealed class Payload {
        class MsgCounterChanged(val newCounter: Int): Payload()
        class SeparatorChanged(val isLast: Boolean): Payload()
    }
}
