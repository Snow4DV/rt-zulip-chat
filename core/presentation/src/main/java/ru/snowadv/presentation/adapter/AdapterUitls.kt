package ru.snowadv.presentation.adapter

import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter

fun setupDiffDelegatesAdapter(vararg delegates: AdapterDelegate<DelegateItem>): DiffDelegationAdapter {
    return DiffDelegationAdapter(setupDelegatesManager(*delegates))
}

fun RecyclerView.ItemAnimator.updateAnimationDurations(lengthMs: Long) {
    removeDuration = lengthMs
    addDuration = lengthMs
    changeDuration = lengthMs
    moveDuration = lengthMs
}

fun <T> ArrayAdapter<T>.updateIfChanged(newList: List<T>) {
    if (count == newList.size) {
        newList.forEachIndexed { index, item ->
            if (getItem(index) != item) {
                update(newList)
                return
            }
        }
    } else {
        update(newList)
    }
}

fun <T> ArrayAdapter<T>.update(newList: List<T>) {
    clear()
    addAll(newList)
}

private fun <T> setupDelegatesManager(vararg delegates: AdapterDelegate<T>): AdapterDelegatesManager<T> {
    return AdapterDelegatesManager(*delegates)
}