package ru.snowadv.home.presentation.model

import ru.snowadv.home.R
import ru.snowadv.presentation.adapter.DelegateItem

internal data class Person(
    override val id: Long,
    val fullName: String,
    val email: String,
    val avatarUrl: String?,
    val status: Status
) : DelegateItem {
    sealed class Payload {
        class StatusChanged(val newStatus: Status) : Payload()
        class AvatarChanged(val newAvatar: String?) : Payload()
    }

    enum class Status(val colorResId: Int, val displayNameResId: Int) {
        ONLINE(R.color.online, R.string.online),
        OFFLINE(R.color.offline, R.string.offline),
        IDLE(R.color.idle, R.string.idle),
    }

    override fun getPayload(oldItem: DelegateItem): Any? {
        if (oldItem is Person && id == oldItem.id && fullName == oldItem.fullName
            && email == oldItem.email && avatarUrl == oldItem.avatarUrl && status != oldItem.status) {
            return Payload.StatusChanged(status)
        }
        if (oldItem is Person && id == oldItem.id && fullName == oldItem.fullName
            && email == oldItem.email && status == oldItem.status && avatarUrl != oldItem.avatarUrl) {
            return Payload.AvatarChanged(avatarUrl)
        }
        return null
    }
}