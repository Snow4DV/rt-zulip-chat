package ru.snowadv.chat.presentation.chat.event

internal sealed class ChatScreenEvent {
    data object SendMessageAddAttachmentButtonClicked: ChatScreenEvent()
    class MessageLongClicked(val messageId: Long, val userId: Long): ChatScreenEvent()
    class AddReactionClicked(val messageId: Long): ChatScreenEvent()
    class AddChosenReaction(val messageId: Long, val reactionName: String): ChatScreenEvent()
    class RemoveReaction(val messageId: Long, val reactionName: String): ChatScreenEvent()
    class GoToProfileClicked(val profileId: Long): ChatScreenEvent()
    class MessageFieldChanged(val text: String): ChatScreenEvent()
    data object GoBackClicked: ChatScreenEvent()
    data object ReloadClicked: ChatScreenEvent()
    data object PaginationLoadMore: ChatScreenEvent()
    data object ScrolledToTop: ChatScreenEvent()
}