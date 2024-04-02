package ru.snowadv.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate<T> {
    fun isForViewType(items: List<T>, position: Int): Boolean
    fun onCreateViewHolder(parent: ViewGroup, getCurrentList: () -> List<T>): RecyclerView.ViewHolder
    fun onBindViewHolder(holder: RecyclerView.ViewHolder, items: List<T>, position: Int, payloads: List<Any>)
}