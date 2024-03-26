package ru.snowadv.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Adapter delegate that does all unsafe casts under the hood.
 * @param <T> Delegate item that all models of list will implement/extend
 * @param <VH> View holder of list item
 * @param <P> Payload parameter. Can be set to Nothing if payloads are not used.
 * P.S. Nothing will break if you pass wrong payload to the item - safe casts are used.
 */
abstract class GenericAdapterDelegate<I : T, T, VH : ViewHolder, P> : AdapterDelegate<T> {
    final override fun isForViewType(items: List<T>, position: Int): Boolean {
        return isForViewType(items[position])
    }

    @Suppress("UNCHECKED_CAST")
    final override fun onBindViewHolder(
        holder: ViewHolder,
        items: List<T>,
        position: Int,
        payloads: List<Any>
    ) {
        // We've already checked if the element can be bound by this in isForViewType function
        onBindViewHolder(
            items[position] as I,
            holder as VH,
            if (payloads.isNotEmpty()) payloads.mapNotNull { it as? P } else emptyList()
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun getItemAtPosition(items: List<T>, position: Int): I? {
        return if (position in items.indices && isForViewType(items[position])) {
            items[position] as I
        } else {
            null
        }
    }

    abstract fun isForViewType(item: T): Boolean
    abstract fun onBindViewHolder(item: I, holder: VH, payloads: List<P>)
}