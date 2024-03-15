package ru.snowadv.presentation.utils

import android.util.TypedValue
import android.view.View

fun View.dimToPx(dimension: Int, unitTypedValue: Int): Float {
    return TypedValue.applyDimension(
        unitTypedValue,
        dimension.toFloat(),
        resources.displayMetrics
    )
}

fun View.dimToPx(dimension: Float, unitTypedValue: Int): Float {
    return TypedValue.applyDimension(
        unitTypedValue,
        dimension,
        resources.displayMetrics
    )
}