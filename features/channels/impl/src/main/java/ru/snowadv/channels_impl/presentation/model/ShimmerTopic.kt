package ru.snowadv.channels_impl.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class ShimmerTopic(
    override val id: Int,
    override val streamId: Long,
) : DelegateItem, StreamIdContainer {

    companion object {
        private const val DEFAULT_COUNT_OF_SHIMMERS = 3
        fun generateShimmers(streamId: Long, count: Int = DEFAULT_COUNT_OF_SHIMMERS) =
            (1..count).map { ShimmerTopic(it, streamId) }
    }
}
