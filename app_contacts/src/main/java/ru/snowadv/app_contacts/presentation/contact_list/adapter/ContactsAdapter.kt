package ru.snowadv.app_contacts.presentation.contact_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.app_contacts.databinding.ItemContactBinding
import ru.snowadv.app_contacts.presentation.model.Contact

internal class ContactsAdapter :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(StaticDiffCallback) {
    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        getItem(position).let { contact ->
            holder.binding.contactName.text = contact.name
            contact.phoneNumbers.firstOrNull()?.let { contactNumber ->
                holder.binding.contactNumber.text = contactNumber
            }
        }
    }

    companion object {
        val StaticDiffCallback by lazy {
            object : DiffUtil.ItemCallback<Contact>() {
                override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                    return oldItem.id == newItem.id
                }
                override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }


}