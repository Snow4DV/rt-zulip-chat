package ru.snowadv.presentation.util

import android.view.View
import kotlin.reflect.KProperty

class ViewInvalidatingProperty<T>(
    private var value: T,
    private val requestLayout: Boolean = false,
    private val refreshDrawableState: Boolean = false,
    private val onChange: ((T) -> Unit)? = null
) {
    operator fun getValue(thisRef: View?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: View?, property: KProperty<*>, value: T) {
        if (this.value != value) {
            this.value = value
            onChange?.invoke(value)

            if (refreshDrawableState) thisRef?.refreshDrawableState()
            if (requestLayout) thisRef?.requestLayout()
            thisRef?.invalidate()
        }
    }
}