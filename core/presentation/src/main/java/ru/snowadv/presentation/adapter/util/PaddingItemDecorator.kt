package ru.snowadv.presentation.adapter.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecorator(
    context: Context,
    horizontalSpacingResId: Int? = null,
    verticalSpacingResId: Int? = null
) : RecyclerView.ItemDecoration() {
    private val horizontalSpacingPx: Int
    private val verticalSpacingPx: Int

    init {
        context.resources.let { resources ->
            horizontalSpacingPx =
                horizontalSpacingResId?.let { resources.getDimensionPixelSize(it) } ?: 0
            verticalSpacingPx =
                verticalSpacingResId?.let { resources.getDimensionPixelSize(it) } ?: 0
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childLayoutPosition = parent.getChildLayoutPosition(view)

        if (childLayoutPosition == 0) {
            outRect.top = horizontalSpacingPx
        }
        outRect.bottom = horizontalSpacingPx
        outRect.right = verticalSpacingPx
        outRect.left =  verticalSpacingPx
    }
}