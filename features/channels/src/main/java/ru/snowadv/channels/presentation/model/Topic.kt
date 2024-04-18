package ru.snowadv.channels.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class Topic(
    val uniqueId: String,
    val name: String,
    override val streamId: Long,
    val position: Int,
) : DelegateItem, StreamIdContainer {
    override val id: Any get() = uniqueId
}
