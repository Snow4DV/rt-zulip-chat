package ru.snowadv.channels_presentation.stream_list.ui.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class UiShimmerTopic(
    override val id: Int,
    override val streamId: Long,
    val isLast: Boolean,
) : DelegateItem, StreamIdContainer {

    companion object {
        private const val DEFAULT_COUNT_OF_SHIMMERS = 3
        fun generateShimmers(streamId: Long, count: Int = DEFAULT_COUNT_OF_SHIMMERS) =
            (1..count).map { UiShimmerTopic(id = it, streamId = streamId, isLast = it == count - 1) }
    }
}
