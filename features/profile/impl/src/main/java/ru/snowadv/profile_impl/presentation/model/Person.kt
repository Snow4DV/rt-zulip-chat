package ru.snowadv.profile_impl.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.profile_impl.R

data class Person(
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
        ONLINE(ru.snowadv.presentation.R.color.online, R.string.online),
        IDLE(ru.snowadv.presentation.R.color.idle, R.string.idle),
        OFFLINE(ru.snowadv.presentation.R.color.offline, R.string.offline),
        UNKNOWN(ru.snowadv.presentation.R.color.light_gray_1, R.string.unknown);
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