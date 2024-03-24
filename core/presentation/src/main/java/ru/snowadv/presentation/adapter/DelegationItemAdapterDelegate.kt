package ru.snowadv.presentation.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * Extension that forces user to pass elements with ids and implement equals function
 * @param <T> Delegate item that all models of list will implement/extend (should implement DelegateItem interface)
 * @param <VH> View holder of list item
 * @param <P> Payload parameter. Can be set to Nothing if payloads are not used.
 * P.S. Nothing will break if you pass wrong payload to the item - safe casts are used.
 */
abstract class DelegationItemAdapterDelegate<T: DelegateItem, VH: RecyclerView.ViewHolder, P>:
    GenericAdapterDelegate<T, DelegateItem, VH, P>() {
}