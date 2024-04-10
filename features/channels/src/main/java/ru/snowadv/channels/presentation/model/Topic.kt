package ru.snowadv.channels.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class Topic(
    val uniqueId: String,
    val name: String,
    val streamId: Long,
    val position: Int,
) : DelegateItem {
    override val id: Any get() = uniqueId
}
