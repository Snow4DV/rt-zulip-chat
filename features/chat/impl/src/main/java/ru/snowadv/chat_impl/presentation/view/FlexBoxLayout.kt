package ru.snowadv.chat_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import ru.snowadv.chat_impl.R
import ru.snowadv.presentation.view.ViewInvalidatingProperty
import ru.snowadv.presentation.view.dimToPx
import kotlin.math.max
import androidx.core.content.res.use

/**
 * This layout follows these rules:
 * 1) Places children horizontally in a row with specified padding between elements
 * 2) If element doesn't fit - it will be placed on the next line with specified padding between lines
 * 3) Params stretchOnLastLine and stretchOnNotLastLines specify if items should be stretched with
 * space between (like flex "space-between" in CSS)
 * 4) Supports margins & paddings
 * 5) If mirror is enabled - views will be placed starting from the right side
 */
internal class FlexBoxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.flexBoxStyle,
    defStyleRes: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val layoutResult = MutableRowLayoutResult()

    companion object {
        const val DEFAULT_PADDING_BETWEEN_ITEMS_DP = 5
        const val DEFAULT_STRETCH_ON_NOT_LAST_LINE = false
        const val DEFAULT_STRETCH_ON_LAST_LINE = false
        const val DEFAULT_MIRROR = false
        const val DEFAULT_FLIP_IN_ROW = false
    }

    var paddingBetweenViewsPx: Float by ViewInvalidatingProperty(
        value = dimToPx(DEFAULT_PADDING_BETWEEN_ITEMS_DP, TypedValue.COMPLEX_UNIT_DIP),
        requestLayout = true,
    )

    var stretchOnLastLine: Boolean by ViewInvalidatingProperty(
        value = DEFAULT_STRETCH_ON_LAST_LINE,
        requestLayout = true
    )
    var stretchOnNotLastLines: Boolean by ViewInvalidatingProperty(
        value = DEFAULT_STRETCH_ON_NOT_LAST_LINE,
        requestLayout = true
    )
    var mirror: Boolean by ViewInvalidatingProperty(
        value = DEFAULT_MIRROR,
        requestLayout = true
    )
    var flipInRow: Boolean by ViewInvalidatingProperty(
        value = DEFAULT_FLIP_IN_ROW,
        requestLayout = true
    )

    init {
        initAttributes(attrs, defStyleAttr, defStyleRes)
    }

    /**
     * Measures children and places them in multiple lines to count new view's height and width
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth = suggestedMinimumWidth + paddingRight + paddingLeft
        val minHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight

        val containerLeft = paddingLeft
        val containerRight = MeasureSpec.getSize(widthMeasureSpec) - paddingRight
        val containerTop = paddingTop

        children.filter { it.visibility != GONE }.forEach { child ->
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
        }

        var linesCount = 0
        var firstNotInflatedIndex = 0
        var maxRowWidth = 0
        var accumulatedHeight = 0
        // Place children in multiple lines and determine how many lines will be needed
        while (firstNotInflatedIndex <= childCount - 1) {
            val rowTopPadding = if (linesCount != 0) paddingBetweenViewsPx.toInt() else 0
            layoutChildrenInOneRow(
                left = containerLeft,
                top = containerTop + accumulatedHeight + rowTopPadding,
                right = containerRight,
                paddingBetweenPx = paddingBetweenViewsPx.toInt(),
                startIndex = firstNotInflatedIndex,
                endIndex = childCount - 1,
                mirror = false,
                flipInRow = false,
                skipLayout = true,
                destLayoutResult = layoutResult
            )

            if(layoutResult.viewsCount == 0) { // Unable to draw any more views
                break
            }

            firstNotInflatedIndex += layoutResult.viewsCount
            maxRowWidth = max(maxRowWidth, layoutResult.rowWidth)
            linesCount++
            accumulatedHeight += layoutResult.rowHeight + rowTopPadding
        }

        // Look at parentWidthStretch settings and find out if we'll take all available width or only max width of lines
        val measuredWidth =
            if (linesCount <= 1 && !stretchOnLastLine || (!stretchOnLastLine && !stretchOnNotLastLines)) {
                (maxRowWidth + paddingLeft + paddingRight)
            } else {
                parentWidth
            }
        // Height is determined by accumulatedHeight param and capped by parentHeight
        val measuredHeight = accumulatedHeight + paddingTop + paddingBottom

        val desiredWidth = max(measuredWidth, minWidth)
        val desiredHeight = max(measuredHeight, minHeight)

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var linesCount = 1
        var firstNoLayoutIndex = 0

        var accumulatedWidth = 0
        var accumulatedWidthWithoutLeftPaddings = 0
        var accumulatedHeight = 0

        var verticalPaddingIfNeeded: Int
        children.filter { it.visibility != GONE }.forEachIndexed { index, view ->
            var horizontalPaddingIfNeeded =
                (if (accumulatedWidth != 0) paddingBetweenViewsPx else 0).toInt()
            verticalPaddingIfNeeded = if (linesCount != 1) paddingBetweenViewsPx.toInt() else 0

            // Measure current view
            val viewWidth = view.measuredWidth + view.marginLeft + view.marginRight
            val childWidthWithPadding =
                viewWidth + horizontalPaddingIfNeeded

            // Place previous views if new one doesn't fit & go to the next line
            if (accumulatedWidth + childWidthWithPadding > measuredWidth) {

                // Stretch buttons with space between items (like "space-between")
                val countOfPaddings = index - firstNoLayoutIndex - 1
                val newPadding =
                    if (countOfPaddings > 0 && stretchOnNotLastLines) {
                        ((measuredWidth - accumulatedWidthWithoutLeftPaddings) / countOfPaddings)
                    } else {
                        paddingBetweenViewsPx.toInt()
                    }

                // Place items in range (firstNoLayoutIndex..index-1)
                accumulatedHeight += layoutChildrenInOneRow(
                    paddingLeft,
                    paddingTop + accumulatedHeight + verticalPaddingIfNeeded,
                    right - paddingRight,
                    newPadding,
                    firstNoLayoutIndex,
                    index - 1,
                    mirror,
                    flipInRow,
                    false,
                    layoutResult
                ).let { layoutResult.rowHeight } + verticalPaddingIfNeeded

                // Reset values for the next line
                accumulatedWidth = 0
                accumulatedWidthWithoutLeftPaddings = 0
                linesCount++
                firstNoLayoutIndex = index
                horizontalPaddingIfNeeded = 0
            }

            accumulatedWidth += viewWidth + horizontalPaddingIfNeeded
            accumulatedWidthWithoutLeftPaddings += viewWidth
        }

        // Determine whether view that are left in last line should also be stretched with space between
        verticalPaddingIfNeeded = if (linesCount != 1) paddingBetweenViewsPx.toInt() else 0
        val lastRowPadding =
            if (firstNoLayoutIndex < childCount - 1 && stretchOnLastLine) {
                val countOfPaddings = childCount - firstNoLayoutIndex - 1
                (measuredWidth - accumulatedWidthWithoutLeftPaddings) / countOfPaddings
            } else {
                paddingBetweenViewsPx.toInt()
            }
        layoutChildrenInOneRow( // Place last row
            paddingLeft,
            paddingTop + accumulatedHeight + verticalPaddingIfNeeded,
            right - paddingRight,
            lastRowPadding,
            firstNoLayoutIndex,
            childCount - 1,
            mirror,
            flipInRow,
            false,
            layoutResult
        )

    }

    /**
     * This method lays children in index range (startIndex..endIndex) in single row
     * @return Returns height of row
     */
    private fun layoutChildrenInOneRow(
        left: Int,
        top: Int,
        right: Int,
        paddingBetweenPx: Int,
        startIndex: Int,
        endIndex: Int,
        mirror: Boolean,
        flipInRow: Boolean,
        skipLayout: Boolean = false,
        destLayoutResult: MutableRowLayoutResult
    ) {
        var curRowHeight = 0
        var accumulatedWidth = 0
        var viewsCount = 0
        loopWithDirection(startIndex = startIndex, endIndex = endIndex, isReversed = flipInRow) {
            val view = getChildAt(it)
            val viewWidth = view.measuredWidth + view.marginLeft + view.marginRight
            val viewHeight = view.measuredHeight + view.marginTop + view.marginBottom

            val paddingBeforeView = if (viewsCount != 0) paddingBetweenPx else 0

            if(accumulatedWidth + paddingBeforeView + viewWidth > right - left) { // Do not place views out of bounds
                destLayoutResult.rowHeight = curRowHeight
                destLayoutResult.rowWidth = accumulatedWidth
                destLayoutResult.viewsCount = viewsCount
                return
            } else if (mirror && !skipLayout) { // Place starting from the right if mirrored
                view.layout(
                    right - viewWidth - accumulatedWidth - paddingBeforeView,
                    top,
                    right - accumulatedWidth - paddingBeforeView,
                    top + viewHeight
                )
            } else if (!skipLayout) {
                view.layout(
                    left + accumulatedWidth + paddingBeforeView,
                    top,
                    left + accumulatedWidth + paddingBeforeView + viewWidth,
                    top + viewHeight
                )
            }
            viewsCount++
            accumulatedWidth += viewWidth + paddingBeforeView
            curRowHeight = max(curRowHeight, viewHeight)
        }

        destLayoutResult.rowHeight = curRowHeight
        destLayoutResult.rowWidth = accumulatedWidth
        destLayoutResult.viewsCount = viewsCount
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.FlexBoxLayout, defStyleAttr, defStyleRes)
            .use { styledAttributes ->
                paddingBetweenViewsPx = styledAttributes.getDimensionPixelSize(
                    R.styleable.FlexBoxLayout_paddingBetweenItems,
                    dimToPx(DEFAULT_PADDING_BETWEEN_ITEMS_DP, TypedValue.COMPLEX_UNIT_DIP).toInt()
                ).toFloat()
                stretchOnLastLine = styledAttributes.getBoolean(
                    R.styleable.FlexBoxLayout_stretchOnLastLine,
                    DEFAULT_STRETCH_ON_LAST_LINE
                )
                stretchOnNotLastLines = styledAttributes.getBoolean(
                    R.styleable.FlexBoxLayout_stretchOnNotLastLines,
                    DEFAULT_STRETCH_ON_NOT_LAST_LINE
                )
                mirror = styledAttributes.getBoolean(
                    R.styleable.FlexBoxLayout_mirror,
                    DEFAULT_MIRROR
                )
                flipInRow = styledAttributes.getBoolean(
                    R.styleable.FlexBoxLayout_flipInRow,
                    DEFAULT_FLIP_IN_ROW
                )
                isSelected
            }
    }

    private inline fun loopWithDirection(
        startIndex: Int,
        endIndex: Int,
        isReversed: Boolean = false,
        action: (Int) -> Unit
    ) {
        if (isReversed) {
            for (i in endIndex downTo startIndex) {
                action(i)
            }
        } else {
            for (i in startIndex..endIndex) {
                action(i)
            }
        }
    }

    /**
     * This class describes the result of placing views in single row
     */
    private class MutableRowLayoutResult(var viewsCount: Int = 0, var rowHeight: Int = 0, var rowWidth: Int = 0)
}