package ru.snowadv.presentation.adapter.impl

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.snowadv.presentation.adapter.AdapterDelegate

class AdapterDelegatesManager<T>(vararg delegates: AdapterDelegate<T>) {
    private val delegates = mutableListOf<AdapterDelegate<T>>()

    init {
        this.delegates.addAll(delegates)
    }

    fun addDelegate(delegate: AdapterDelegate<T>): Int {
        delegates.add(delegate)
        return delegates.indices.last
    }

    fun getItemViewType(items: List<T>, position: Int): Int {
        return delegates.indexOfFirst { it.isForViewType(items, position) }.also { index ->
            if (index == -1) {
                throw IllegalArgumentException("No such delegate for item at position $position")
            }
        }
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int, getCurrentList: () -> List<T>): ViewHolder {
        return getDelegateForViewTypeOrThrow(viewType).onCreateViewHolder(parent, getCurrentList)
    }

    fun onBindViewHolder(items: List<T>, position: Int, holder: ViewHolder, payloads: List<Any>) {
        return getDelegateForViewTypeOrThrow(holder.itemViewType).onBindViewHolder(
            holder,
            items,
            position,
            payloads
        )
    }

    fun onViewAttachedToWindow(holder: ViewHolder, getCurrentList: () -> List<T>) {
        getDelegateForViewTypeOrThrow(holder.itemViewType).onViewAttachedToWindow(
            holder,
            getCurrentList,
        )
    }

    fun onViewDetachedFromWindow(holder: ViewHolder, getCurrentList: () -> List<T>) {
        getDelegateForViewTypeOrThrow(holder.itemViewType).onViewDetachedFromWindow(
            holder,
            getCurrentList,
        )
    }

    private fun getDelegateForViewTypeOrThrow(viewType: Int) =
        delegates.getOrNull(viewType) ?: error("No such delegate with index $viewType")
}