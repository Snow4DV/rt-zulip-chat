package ru.snowadv.people_presentation.ui.model

import ru.snowadv.presentation.adapter.DelegateItem

data class UiPerson(
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

    enum class Status(val colorResId: Int) {
        ONLINE(ru.snowadv.presentation.R.color.online),
        IDLE(ru.snowadv.presentation.R.color.idle),
        OFFLINE(ru.snowadv.presentation.R.color.offline),
        UNKNOWN(ru.snowadv.presentation.R.color.light_gray_1),
    }

    override fun getPayload(oldItem: DelegateItem): Any? {
        if (oldItem is UiPerson && id == oldItem.id && fullName == oldItem.fullName
            && email == oldItem.email && avatarUrl == oldItem.avatarUrl && status != oldItem.status) {
            return Payload.StatusChanged(status)
        }
        if (oldItem is UiPerson && id == oldItem.id && fullName == oldItem.fullName
            && email == oldItem.email && status == oldItem.status && avatarUrl != oldItem.avatarUrl) {
            return Payload.AvatarChanged(avatarUrl)
        }
        return null
    }
}