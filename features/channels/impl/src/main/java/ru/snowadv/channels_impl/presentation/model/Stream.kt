package ru.snowadv.channels_impl.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class Stream(
    override val id: Long,
    val name: String,
    val expanded: Boolean,
): DelegateItem {

    override fun getPayload(oldItem: DelegateItem): Any? {
        if (oldItem is Stream && id == oldItem.id && name == oldItem.name && expanded != oldItem.expanded) {
            return Payload.ExpandedChanged(expanded)
        }
        return null
    }

    sealed class Payload {
        class ExpandedChanged(val expanded: Boolean): Payload()
    }
}