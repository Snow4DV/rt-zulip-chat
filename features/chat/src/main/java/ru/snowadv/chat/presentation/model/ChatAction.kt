package ru.snowadv.chat.presentation.model

import ru.snowadv.chat.R

sealed class ChatAction(val iconResId: Int, val titleResId: Int) {
    class AddReaction(val messageId: Long) :
        ChatAction(R.drawable.ic_add_reaction, R.string.add_reaction_action)

    class OpenProfile(val userId: Long) :
        ChatAction(R.drawable.ic_profile, R.string.open_profile_action)


    companion object {
        fun createActionsForMessage(messageId: Long, userId: Long): List<ChatAction> {
            return listOf(AddReaction(messageId), OpenProfile(userId))
        }
    }
}