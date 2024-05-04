package ru.snowadv.presentation.adapter.impl

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

sealed class DelegationAdapter<T>(
    protected val delegatesManager: AdapterDelegatesManager<T>,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType, ::getCurrentList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        delegatesManager.onBindViewHolder(currentList, position, holder, payloads)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        delegatesManager.onViewAttachedToWindow(holder, ::getCurrentList)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder, ::getCurrentList)
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(currentList, position)
    }
}