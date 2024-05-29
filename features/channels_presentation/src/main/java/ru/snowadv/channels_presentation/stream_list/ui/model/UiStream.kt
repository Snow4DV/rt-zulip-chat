package ru.snowadv.channels_presentation.stream_list.ui.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class UiStream(
    override val id: Long,
    val name: String,
    val expanded: Boolean,
    val subscribeStatus: SubscribeStatus,
    val color: String = DEFAULT_UNREAD_MESSAGES_TEXT_COLOR,
    val unreadMessagesCount: Int,
) : DelegateItem {

    companion object {
        const val DEFAULT_UNREAD_MESSAGES_TEXT_COLOR = "#2A9D8F"
    }

    override fun getPayload(oldItem: DelegateItem): Any? {
        return when {
            oldItem is UiStream && id == oldItem.id && name == oldItem.name
                    && subscribeStatus == oldItem.subscribeStatus && color == oldItem.color &&
                    unreadMessagesCount == oldItem.unreadMessagesCount && expanded != oldItem.expanded -> {
                Payload.ExpandedChanged(expanded)
            }
            oldItem is UiStream && id == oldItem.id && name == oldItem.name
                    && subscribeStatus != oldItem.subscribeStatus && color == oldItem.color &&
                    unreadMessagesCount == oldItem.unreadMessagesCount && expanded == oldItem.expanded -> {
                Payload.SubscribedStatusChanged(subscribeStatus)
            }
            oldItem is UiStream && id == oldItem.id && name == oldItem.name
                    && subscribeStatus == oldItem.subscribeStatus && color == oldItem.color &&
                    unreadMessagesCount != oldItem.unreadMessagesCount && expanded == oldItem.expanded -> {
                Payload.UnreadMessagesCountChanged(unreadMessagesCount)
            }
            else -> null
        }
    }

    sealed class Payload {
        class ExpandedChanged(val expanded: Boolean) : Payload()
        class SubscribedStatusChanged(val newStatus: SubscribeStatus) : Payload()
        class UnreadMessagesCountChanged(val newCount: Int) : Payload()
    }

    enum class SubscribeStatus(val subscribed: Boolean, val loading: Boolean) {
        NOT_SUBSCRIBED(subscribed = false, loading = false),
        SUBSCRIBING(subscribed = false, loading = true),
        SUBSCRIBED(subscribed = true, loading = false);
    }
}