package ru.snowadv.app_contacts.presentation.contact_list.adapter

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.app_contacts.databinding.ItemContactBinding
import ru.snowadv.app_contacts.domain.model.Contact

class ContactAdapter(contacts: List<Contact>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    var contacts = contacts
        private set

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.binding.contactName.text = contacts[position].name
        contacts[position].phoneNumbers.firstOrNull()?.let {
            holder.binding.contactNumber.text = it
        }
    }

    fun setContacts(contacts: List<Contact>) {
        val contactsCallback = Callback(this.contacts, contacts)
        val diffResult = DiffUtil.calculateDiff(contactsCallback)
        this.contacts = contacts
        diffResult.dispatchUpdatesTo(this)
    }

    class Callback(
        private val old: List<Contact>,
        private val new: List<Contact>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return old.size
        }

        override fun getNewListSize(): Int {
            return new.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].id == new[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition] == new[newItemPosition]
        }

    }
}