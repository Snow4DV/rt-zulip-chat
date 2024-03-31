package ru.snowadv.presentation.adapter

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

private fun <T> setupDelegatesManager(vararg delegates: AdapterDelegate<T>): AdapterDelegatesManager<T> {
    return AdapterDelegatesManager(*delegates)
}