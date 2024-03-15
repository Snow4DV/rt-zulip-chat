package ru.snowadv.presentation.utils

import android.view.View
import kotlin.reflect.KProperty

class ViewInvalidatingProperty<T>(
    private var value: T,
    private val requestLayout: Boolean = false,
    private val refreshDrawableState: Boolean = false,
    private val whenChanged: ((T) -> Unit)? = null
) {
    operator fun getValue(thisRef: View?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: View?, property: KProperty<*>, value: T) {
        if (requestLayout && this.value != value) {
            this.value = value
            whenChanged?.invoke(value)
            if(refreshDrawableState) thisRef?.refreshDrawableState()
            thisRef?.invalidate()
            thisRef?.requestLayout()
        } else if (this.value != value) {
            this.value = value
            whenChanged?.invoke(value)
            if(refreshDrawableState) thisRef?.refreshDrawableState()
            thisRef?.invalidate()
        }
    }
}