package ru.snowadv.presentation.adapter.impl

import androidx.recyclerview.widget.DiffUtil
import ru.snowadv.presentation.adapter.DelegateItem

class DiffDelegationAdapter(
    delegatesManager: AdapterDelegatesManager<DelegateItem>,
) : DelegationAdapter<DelegateItem>(delegatesManager, delegateItemDiffUtilCallback) {

    companion object {
        private val delegateItemDiffUtilCallback by lazy { DelegateItemCallback() }
    }

    private class DelegateItemCallback: DiffUtil.ItemCallback<DelegateItem>() {
        override fun areItemsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean {
            return oldItem::class == newItem::class && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean {
            return oldItem == newItem
        }

    }
}