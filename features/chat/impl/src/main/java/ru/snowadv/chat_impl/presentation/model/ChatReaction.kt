package ru.snowadv.chat_impl.presentation.model

import ru.snowadv.presentation.adapter.DelegateItem

internal data class ChatReaction(
    val name: String,
    val emojiCode: String,
    val count: Int,
    val userReacted: Boolean,
    val emojiString: String,
): DelegateItem {
    override val id: String = emojiCode
    override fun getPayload(oldItem: DelegateItem): Payload? {
        if (oldItem !is ChatReaction) return null

        return if (oldItem.name == name && oldItem.emojiCode == emojiCode
            && oldItem.userReacted == userReacted && oldItem.emojiString == emojiString
            && oldItem.count != count) {
            Payload.ChangedCount(count)
        } else if (oldItem.name == name && oldItem.emojiCode == emojiCode
            && oldItem.userReacted != userReacted && oldItem.emojiString == emojiString
            && oldItem.count == count) {
            Payload.ChangedUserReacted(userReacted)
        } else {
            null
        }
    }

    sealed interface Payload {
        class ChangedCount(val newCount: Int): Payload
        class ChangedUserReacted(val newUserReacted: Boolean): Payload
    }
}