package ru.snowadv.presentation.view

import android.util.TypedValue
import android.view.View
import androidx.core.util.TypedValueCompat

fun View.dimToPx(dimension: Int, @TypedValueCompat.ComplexDimensionUnit unitTypedValue: Int): Float {
    return TypedValue.applyDimension(
        unitTypedValue,
        dimension.toFloat(),
        resources.displayMetrics
    )
}

fun View.dimToPx(dimension: Float, @TypedValueCompat.ComplexDimensionUnit unitTypedValue: Int): Float {
    return TypedValue.applyDimension(
        unitTypedValue,
        dimension,
        resources.displayMetrics
    )
}

fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}