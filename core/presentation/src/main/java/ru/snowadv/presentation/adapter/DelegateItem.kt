package ru.snowadv.presentation.adapter

interface DelegateItem {
    val id: Any
    override fun equals(other: Any?): Boolean
    open fun getPayload(oldItem: DelegateItem): Any? {
        return null
    }
}