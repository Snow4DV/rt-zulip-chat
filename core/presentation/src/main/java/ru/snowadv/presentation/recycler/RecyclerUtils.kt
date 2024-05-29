package ru.snowadv.presentation.recycler

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.presentation.R
import ru.snowadv.presentation.adapter.util.PaddingItemDecorator

fun RecyclerView.setupDefaultDecorator() {
    addItemDecoration(getDefaultItemDecoration())
}

fun RecyclerView.setupDecorator(
    horizontalSpacingResId: Int? = null,
    verticalSpacingResId: Int? = null,
) {
    addItemDecoration(
        PaddingItemDecorator(
            context,
            horizontalSpacingResId ?: R.dimen.small_common_padding,
            verticalSpacingResId ?: R.dimen.small_common_padding,
        )
    )
}
private fun RecyclerView.getDefaultItemDecoration(): PaddingItemDecorator {
    return PaddingItemDecorator(
        context,
        R.dimen.small_common_padding,
        R.dimen.small_common_padding
    )
}