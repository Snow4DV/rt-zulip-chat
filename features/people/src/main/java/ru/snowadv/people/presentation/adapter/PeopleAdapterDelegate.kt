package ru.snowadv.people.presentation.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import ru.snowadv.people.databinding.ItemPersonBinding
import ru.snowadv.people.presentation.model.Person
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.DelegationItemAdapterDelegate

internal class PeopleAdapterDelegate(
    private val onPersonClickListener: ((Person) -> Unit)? = null,
) :
    DelegationItemAdapterDelegate<Person, PeopleAdapterDelegate.PersonViewHolder, Person.Payload>() {
    internal inner class PersonViewHolder(private val binding: ItemPersonBinding) :
        ViewHolder(binding.root) {
        fun initClickListeners(getCurrentList: () -> List<DelegateItem>) = with(binding) {
            root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    getItemAtPosition(getCurrentList(), adapterPosition)?.let {
                        onPersonClickListener?.invoke(it)
                    }
                }
            }
        }

        fun bind(person: Person) = with(binding) {
            userName.text = person.fullName
            userEmail.text = person.email
            bindAvatarUrl(person.avatarUrl)
            bindStatus(person.status)
        }

        fun handlePayload(payload: Person.Payload) {
            when (payload) {
                is Person.Payload.AvatarChanged -> bindAvatarUrl(payload.newAvatar)
                is Person.Payload.StatusChanged -> bindStatus(payload.newStatus)
            }
        }

        private fun bindAvatarUrl(avatarUrl: String?) = with(binding) {
            userAvatar.setImageResource(ru.snowadv.presentation.R.drawable.ic_user_avatar)
            avatarUrl?.let { url ->
                userAvatar.load(url) {
                    placeholder(ru.snowadv.presentation.R.drawable.ic_user_avatar)
                    crossfade(true)
                }
            }
        }

        private fun bindStatus(newStatus: Person.Status) = with(binding) {
            this.userStatus.setImageDrawable(
                ColorDrawable(
                    root.resources.getColor(
                        newStatus.colorResId,
                        root.context.theme
                    )
                )
            )
        }
    }

    override fun isForViewType(item: DelegateItem): Boolean {
        return item is Person
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        getCurrentList: () -> List<DelegateItem>
    ): ViewHolder {
        return PersonViewHolder(
            ItemPersonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).also { streamViewHolder ->
            streamViewHolder.initClickListeners(getCurrentList)
        }
    }

    override fun onBindViewHolder(
        item: Person,
        holder: PersonViewHolder,
        payloads: List<Person.Payload>
    ) {
        if (payloads.isEmpty()) {
            holder.bind(item)
        } else {
            payloads.forEach(holder::handlePayload)
        }
    }
}